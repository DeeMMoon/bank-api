package com.sbt.bank.api.services;

import com.sbt.bank.api.models.Account;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IAccountService {
    Optional<Account> findAccountById(UUID id);
    BigDecimal getAccountAmount(String accountNumber);
    Account addDepositToTheAccountBalance(Account account, BigDecimal amount);
    Optional<Account> findAccountByAccountNumber(String accountNumber);
    Account findAccountByAccountNumber(String accountNumber, List<Account> accounts);
}
