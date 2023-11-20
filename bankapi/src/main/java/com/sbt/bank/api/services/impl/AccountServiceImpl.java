package com.sbt.bank.api.services.impl;

import com.sbt.bank.api.exceptions.account.AccountNotFoundException;
import com.sbt.bank.api.exceptions.account.TooLongTimeGenerationAccountNumberException;
import com.sbt.bank.api.models.Account;
import com.sbt.bank.api.models.Client;
import com.sbt.bank.api.models.Currency;
import com.sbt.bank.api.repositories.AccountRepository;
import com.sbt.bank.api.repositories.ClientRepository;
import com.sbt.bank.api.services.IAccountService;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class AccountServiceImpl implements IAccountService {

    private final AccountRepository accountRepository;

    public AccountServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public Optional<Account> findAccountById(UUID id){
        return accountRepository.findAccountById(id);
    }

    @Override
    public BigDecimal getAccountAmount(String accountNumber){
        Optional<Account> account = accountRepository.findAccountByAccountNumber(accountNumber);
        if (account.isPresent()){
            return account.get().getAmount();
        }
        throw new AccountNotFoundException("Can't get amount, because account with with number" + accountNumber + " not found");
    }

    @Override
    @Transactional
    public Account addDepositToTheAccountBalance(Account account, BigDecimal amount){
        if (account == null){
            throw new AccountNotFoundException("Can't get amount, because account not found");
        }
        account.setAmount(account.getAmount().add(amount));
        return account;
    }

    @Override
    public Optional<Account> findAccountByAccountNumber(String accountNumber) {
        return accountRepository.findAccountByAccountNumber(accountNumber);
    }

    @Override
    public Account findAccountByAccountNumber(String accountNumber, List<Account> accounts) {
        return  accounts.stream()
                .filter(p -> p.getAccountNumber().equals(accountNumber))
                .findAny()
                .orElseThrow(() -> new AccountNotFoundException("Account with number: " + accountNumber + " not found"));
    }

    @Transactional
    @Override
    public Account createAccount(Client client, Currency currency) {
        String accountNumber = accountNumberGenerator();
        LocalDateTime endTimeGenerationAccountNumber = LocalDateTime.now().plusMinutes(1);
        while (!checkGeneratedAccountNumberIsUnique(accountNumber)){
            accountNumber = accountNumberGenerator();
            if (LocalDateTime.now().isAfter(endTimeGenerationAccountNumber)){
                throw new TooLongTimeGenerationAccountNumberException("The waiting time for generating the account number is too long");
            }
        }
        var account = new Account().builder()
                .id(UUID.randomUUID())
                .accountNumber(accountNumber)
                .amount(new BigDecimal(0))
                .client(client)
                .currency(currency)
                .isBlocked(false)
                .build();
        return accountRepository.save(account);
    }

    private String accountNumberGenerator(){
        return RandomStringUtils.randomNumeric(20);
    }

    private Boolean checkGeneratedAccountNumberIsUnique(String accountNumber){
        return accountRepository.findAccountByAccountNumber(accountNumber).isEmpty();
    }

}
