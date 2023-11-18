package com.sbt.bank.api.services;

import com.sbt.bank.api.dto.TransactionDTO;
import com.sbt.bank.api.exceptions.account.AccountNotFoundException;
import com.sbt.bank.api.exceptions.transaction.IllegalTransactionAmountException;
import com.sbt.bank.api.exceptions.transaction.IllegalTransactionValidationException;
import com.sbt.bank.api.models.Account;
import com.sbt.bank.api.models.Transaction;
import com.sbt.bank.api.models.TransactionStatus;
import com.sbt.bank.api.repositories.TransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class TransactionServiceImpl implements ITransactionService{

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
    public void doTransaction(TransactionDTO transactionDTO) {
        // ToDo убрать методы update и create, сделать их вызовы в контроллере
        Transaction transaction = createTransaction(transactionDTO);
        updateTransactionStatus(TransactionStatus.PROCESSED, transaction);
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
        transaction.setTransactionStatus(status);
        transactionRepository.save(transaction);
        return Optional.of(transaction);
    }

    @Transactional
    public Transaction createTransaction(TransactionDTO transactionDTO) {
        beforeTransactionValidation(transactionDTO);
        Transaction transaction = initTransaction(transactionDTO);
        afterTransactionValidation(transaction);
        transactionRepository.save(transaction);
        return transaction;
    }


    private Transaction initTransaction(TransactionDTO transactionDTO){
        var currencySender = accountService.findAccountByAccountNumber(transactionDTO.accountNumberSender()).get().getCurrency();
        var currencyRecipient = accountService.findAccountByAccountNumber(transactionDTO.accountNumberRecipient()).get().getCurrency();
        Transaction transaction = new Transaction().builder()
                .amount(transactionDTO.amount())
                .accountNumberSender(transactionDTO.accountNumberSender())
                .accountNumberRecipient(transactionDTO.accountNumberRecipient())
                .id(UUID.randomUUID())
                .transactionStatus(TransactionStatus.START)
                .sendingTime(LocalDateTime.now())
                .enrollmentTime(LocalDateTime.now())
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
       if (transaction.amount().compareTo(BigDecimal.ZERO) < 0
               || accountSender.get().getAmount().subtract(transaction.amount()).compareTo(BigDecimal.ZERO) < 0){
            throw new IllegalTransactionAmountException("Amount should be more zero and sender's balance more then amount");
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

    private void afterTransactionValidation(Transaction transaction){
        if (transactionRepository.findTransactionById(transaction.getId()).isPresent()){
            throw new IllegalTransactionValidationException("Transaction with id = " + transaction.getId() + "already exist");
        }
    }
}
