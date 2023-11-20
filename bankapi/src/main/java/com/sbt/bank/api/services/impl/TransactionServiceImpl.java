package com.sbt.bank.api.services.impl;

import com.sbt.bank.api.dto.TransactionDTO;
import com.sbt.bank.api.exceptions.account.AccountIsBlockedException;
import com.sbt.bank.api.exceptions.account.AccountNotFoundException;
import com.sbt.bank.api.exceptions.transaction.IllegalTransactionAmountException;
import com.sbt.bank.api.exceptions.transaction.IllegalTransactionValidationException;
import com.sbt.bank.api.models.Account;
import com.sbt.bank.api.models.Transaction;
import com.sbt.bank.api.models.TransactionStatus;
import com.sbt.bank.api.repositories.TransactionRepository;
import com.sbt.bank.api.services.IAccountService;
import com.sbt.bank.api.services.ICurrencyRateService;
import com.sbt.bank.api.services.ITransactionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class TransactionServiceImpl implements ITransactionService {

    private final TransactionRepository transactionRepository;
    private final IAccountService accountService;
    private final ICurrencyRateService currencyRateService;

    public TransactionServiceImpl(TransactionRepository transactionRepository, IAccountService accountService, ICurrencyRateService currencyRateService) {
        this.transactionRepository = transactionRepository;
        this.accountService = accountService;
        this.currencyRateService = currencyRateService;
    }

    @Override
    public Optional<Transaction> findTransactionById(UUID id) {
        return transactionRepository.findTransactionById(id);
    }


    @Override
    @Transactional
    public void doTransaction(Transaction transaction) {
        beforeTransactionValidation(TransactionDTO.map(transaction));
        var accountSender = accountService.findAccountByAccountNumber(transaction.getAccountNumberSender());
        var accountRecipient = accountService.findAccountByAccountNumber(transaction.getAccountNumberRecipient());
        accountSender.get().setAmount(accountSender.get().getAmount().subtract(transaction.getAmount()));
        BigDecimal convertedAmount = conversionCurrency(accountSender.get(),accountRecipient.get()).multiply(transaction.getAmount());
        accountRecipient.get().setAmount(accountRecipient.get().getAmount().add(convertedAmount));
        updateTransactionStatus(TransactionStatus.ACCEPTED, transaction);
    }

    @Override
    @Transactional
    public Optional<Transaction> updateTransactionStatus(TransactionStatus status, Transaction transaction) {
        if (transaction.getTransactionStatus() == status){
            return Optional.of(transaction);
        }
        transaction.setUpdatedStatusTime(LocalDateTime.now());
        transaction.setTransactionStatus(status);
        transactionRepository.save(transaction);
        return Optional.of(transaction);
    }

    @Override
    @Transactional
    public Transaction createTransaction(TransactionDTO transactionDTO) {
        beforeTransactionValidation(transactionDTO);
        Transaction transaction = initTransaction(transactionDTO);
        if (transactionRepository.findTransactionById(transaction.getId()).isPresent()){
            throw new IllegalTransactionValidationException("Transaction with id = " + transaction.getId() + "already exist");
        }
        return transactionRepository.save(transaction);
    }


    private Transaction initTransaction(TransactionDTO transactionDTO){
        var currencySender = accountService.findAccountByAccountNumber(transactionDTO.accountNumberSender()).get().getCurrency();
        var currencyRecipient = accountService.findAccountByAccountNumber(transactionDTO.accountNumberRecipient()).get().getCurrency();
        Transaction transaction = new Transaction().builder()
                .amount(transactionDTO.amount().multiply(new BigDecimal(100)))
                .accountNumberSender(transactionDTO.accountNumberSender())
                .accountNumberRecipient(transactionDTO.accountNumberRecipient())
                .id(UUID.randomUUID())
                .transactionStatus(TransactionStatus.START)
                .sendingTime(LocalDateTime.now())
                .updatedStatusTime(LocalDateTime.now())
                .currencyTypeSender(currencySender)
                .currencyTypeRecipient(currencyRecipient)
                .build();
        return transaction;
    }

    private void beforeTransactionValidation(TransactionDTO transaction){
       var accountSender = accountService.findAccountByAccountNumber(transaction.accountNumberSender());
       var accountRecipient = accountService.findAccountByAccountNumber(transaction.accountNumberRecipient());
       if (accountSender.isEmpty()){
           throw new AccountNotFoundException("Can't create transfer because account with number: "
                   + transaction.accountNumberSender() + " not found");
       }
       if (accountRecipient.isEmpty()){
            throw new AccountNotFoundException("Can't create transfer because account with number: "
                    + transaction.accountNumberRecipient() + " not found");
       }
       var checkAmount = transaction.amount().multiply(BigDecimal.valueOf(100.00));
       if (checkAmount.compareTo(BigDecimal.ZERO) <= 0
               || accountSender.get().getAmount().subtract(checkAmount).compareTo(BigDecimal.ZERO) < 0){
            throw new IllegalTransactionAmountException("Amount should be more zero and sender's balance more then amount");
       }
        if (accountSender.get().getIsBlocked()){
            throw new AccountIsBlockedException("This account is blocked, you can't do transfer");
        }
        if (accountRecipient.get().getIsBlocked()){
            throw new AccountIsBlockedException("Recipient account is blocked, you can't do transfer");
        }
    }

    private BigDecimal conversionCurrency(Account accountSender, Account accountRecipient){
        var senderCurrency = accountSender.getCurrency();
        var recipientCurrency = accountRecipient.getCurrency();
        if (senderCurrency == recipientCurrency){
            return BigDecimal.ONE;
        }
        return currencyRateService.getRate(senderCurrency,recipientCurrency);
    }
}
