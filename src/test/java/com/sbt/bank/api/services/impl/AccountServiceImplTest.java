package com.sbt.bank.api.services.impl;

import com.sbt.bank.api.exceptions.account.AccountNotFoundException;
import com.sbt.bank.api.exceptions.account.TooLongTimeGenerationAccountNumberException;
import com.sbt.bank.api.models.Account;
import com.sbt.bank.api.models.Client;
import com.sbt.bank.api.models.Currency;
import com.sbt.bank.api.repositories.AccountRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.reset;

@ExtendWith(MockitoExtension.class)
class AccountServiceImplTest {

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private AccountServiceImpl accountService;

    @AfterEach
    public void resetMocks() {
        reset(accountRepository);
    }

    @Test
    void addDepositToTheAccountBalance() {
        var account1 = new Account(UUID.randomUUID(), "12345678910111213111", Currency.RUR, BigDecimal.valueOf(100), false, new Client());
        doReturn(account1).when(accountRepository).save(account1);

        var responseEntity = accountService.addDepositToTheAccountBalance(Optional.of(account1), BigDecimal.valueOf(100));

        assertNotNull(responseEntity);
        assertEquals(responseEntity.getAmount(), BigDecimal.valueOf(200));
    }

    @Test
    void addDepositToTheAccountBalanceAccountNotFoundException() {
        Optional<Account> account = Optional.empty();
        assertThrows(AccountNotFoundException.class, () -> {
            var responseEntity = accountService.addDepositToTheAccountBalance(account, BigDecimal.valueOf(100));
        });
    }

    @Test
    void findAccountByAccountNumber() {
        var account = new Account(UUID.randomUUID(), "12345678910111213111", Currency.RUR, BigDecimal.valueOf(100), true, new Client());
        doReturn(Optional.of(account)).when(accountRepository).findAccountByAccountNumber(account.getAccountNumber());
        var responseEntity = accountService.findAccountByAccountNumber("12345678910111213111");
        assertNotNull(responseEntity.get());
        assertEquals(responseEntity, Optional.of(account));
        assertEquals(responseEntity.get().getAccountNumber(), "12345678910111213111");
    }

    @Test
    void findAccountByAccountNumberFailTest() {
        doReturn(Optional.empty()).when(accountRepository).findAccountByAccountNumber("12345678910111213112");
        var responseEntity = accountService.findAccountByAccountNumber("12345678910111213112");
        assertTrue(responseEntity.isEmpty());
    }

    @Test
    void testFindAccountByAccountNumber() {
        var account1 = new Account(UUID.randomUUID(), "12345678910111213111", Currency.RUR, BigDecimal.valueOf(100), false, new Client());
        var account2 = new Account(UUID.randomUUID(), "12345678910111213112", Currency.RUR, BigDecimal.valueOf(100), false, new Client());
        var account3 = new Account(UUID.randomUUID(), "12345678910111213113", Currency.RUR, BigDecimal.valueOf(100), false, new Client());

        var accounts = List.of(account1, account2, account3);
        var responseEntity1 = accountService.findAccountByAccountNumber("12345678910111213111", accounts);
        var responseEntity2 = accountService.findAccountByAccountNumber("12345678910111213112", accounts);
        var responseEntity3 = accountService.findAccountByAccountNumber("12345678910111213113", accounts);

        assertNotNull(responseEntity1);
        assertNotNull(responseEntity2);
        assertNotNull(responseEntity3);
        assertEquals(responseEntity1, account1);
        assertEquals(responseEntity2, account2);
        assertEquals(responseEntity3, account3);
        assertNotEquals(responseEntity1, account2);
    }

    @Test
    void testFindAccountByAccountNumberAccountNotFoundException() {
        var account1 = new Account(UUID.randomUUID(), "12345678910111213111", Currency.RUR, BigDecimal.valueOf(100), false, new Client());

        var accounts = List.of(account1);
        var responseEntity1 = accountService.findAccountByAccountNumber("12345678910111213111", accounts);

        assertNotNull(responseEntity1);
        assertThrows(AccountNotFoundException.class, () -> {
            accountService.findAccountByAccountNumber("12345678910111213112", accounts);
            accountService.findAccountByAccountNumber(" ", accounts);
        });

    }

    @Test
    void createAccount() {
        var client = new Client();
        var currency = Currency.RUR;
        var account = new Account(UUID.randomUUID(), "12345678910111213111", currency, BigDecimal.valueOf(100), false, client);
        doReturn(Optional.empty()).when(accountRepository).findAccountByAccountNumber(anyString());
        doReturn(account).when(accountRepository).save(any(Account.class));
        var responseEntity = accountService.createAccount(client, currency);
        assertNotNull(responseEntity);
        assertEquals(account, responseEntity);
    }

    @Disabled
    @Test
    void createAccountTooLongTimeGenerationAccountNumberException() {
        var client = new Client();
        var currency = Currency.RUR;
        doReturn(Optional.of("0")).when(accountRepository).findAccountByAccountNumber(anyString());
        // Проверка не валидной генерации номера счёта(ожидание 1 мин.)
        assertThrows(TooLongTimeGenerationAccountNumberException.class, () -> {
            accountService.createAccount(client, currency);
        });
    }
}