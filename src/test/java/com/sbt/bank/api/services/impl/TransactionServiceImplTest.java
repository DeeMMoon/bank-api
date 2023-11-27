package com.sbt.bank.api.services.impl;

import com.sbt.bank.api.dto.TransactionDTO;
import com.sbt.bank.api.dto.TransactionStatusDTO;
import com.sbt.bank.api.exceptions.account.AccountIsBlockedException;
import com.sbt.bank.api.exceptions.account.AccountNotFoundException;
import com.sbt.bank.api.exceptions.transaction.TransactionAmountException;
import com.sbt.bank.api.exceptions.transaction.TransactionException;
import com.sbt.bank.api.exceptions.transaction.TransactionNotFoundException;
import com.sbt.bank.api.exceptions.transaction.TransactionStatusNotFoundException;
import com.sbt.bank.api.exceptions.transaction.TransactionValidationException;
import com.sbt.bank.api.models.Account;
import com.sbt.bank.api.models.Client;
import com.sbt.bank.api.models.Currency;
import com.sbt.bank.api.models.Transaction;
import com.sbt.bank.api.models.TransactionStatus;
import com.sbt.bank.api.repositories.TransactionRepository;
import com.sbt.bank.api.services.IAccountService;
import com.sbt.bank.api.services.ICurrencyRateService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class TransactionServiceImplTest {

    @Mock
    private TransactionRepository transactionRepository;
    @Mock
    private IAccountService accountService;
    @Mock
    private ICurrencyRateService currencyRateService;
    @InjectMocks
    private TransactionServiceImpl transactionService;

    @AfterEach
    public void resetMocks() {
        reset(transactionRepository);
        reset(accountService);
        reset(currencyRateService);
    }

    @Test
    void updateTransactionStatus() {
        var transaction = new Transaction(UUID.randomUUID(), "", "", BigDecimal.TEN, Currency.RUR, Currency.USD, LocalDateTime.now(), LocalDateTime.now(), TransactionStatus.START);
        doReturn(transaction).when(transactionRepository).save(transaction);

        var responseEntity = transactionService.updateTransactionStatus(TransactionStatus.PROCESSED, transaction);

        assertNotNull(responseEntity);
        assertEquals(responseEntity.get().getTransactionStatus(), TransactionStatus.PROCESSED);
    }

    @Test
    void updateTransactionSameStatus() {
        var transaction = new Transaction(UUID.randomUUID(), "", "", BigDecimal.TEN, Currency.RUR, Currency.USD, LocalDateTime.now(), LocalDateTime.now(), TransactionStatus.START);

        var responseEntity = transactionService.updateTransactionStatus(TransactionStatus.START, transaction);

        assertNotNull(responseEntity);
        assertEquals(responseEntity.get().getTransactionStatus(), TransactionStatus.START);
        verify(transactionRepository, times(0)).save(transaction);
    }

    @Test
    void createTransaction() {
        var account = new Account(UUID.randomUUID(), "12345678910111213111", Currency.RUR, BigDecimal.valueOf(10000), false, new Client());
        var account1 = new Account(UUID.randomUUID(), "12345678910111213112", Currency.RUR, BigDecimal.valueOf(10000), false, new Client());

        var transactionDTO = new TransactionDTO("12345678910111213111", "12345678910111213112", new BigDecimal(50));
        var expectedTransaction = new Transaction(UUID.randomUUID(), "12345678910111213111", "12345678910111213112", BigDecimal.valueOf(50), Currency.RUR, Currency.RUR, LocalDateTime.now(), LocalDateTime.now(), TransactionStatus.START);

        doReturn(Optional.of(account)).when(accountService).findAccountByAccountNumber(account.getAccountNumber());
        doReturn(Optional.of(account1)).when(accountService).findAccountByAccountNumber(account1.getAccountNumber());
        doReturn(Optional.empty()).when(transactionRepository).findTransactionById(any(UUID.class));
        doReturn(expectedTransaction).when(transactionRepository).save(any(Transaction.class));

        var responseEntity = transactionService.createTransaction(transactionDTO);

        assertNotNull(responseEntity);
        assertEquals(responseEntity.getTransactionStatus(), expectedTransaction.getTransactionStatus());
        assertEquals(responseEntity.getAmount(), expectedTransaction.getAmount());
        assertEquals(responseEntity.getAccountNumberRecipient(), expectedTransaction.getAccountNumberRecipient());
        assertEquals(responseEntity.getAccountNumberSender(), expectedTransaction.getAccountNumberSender());
        assertEquals(responseEntity.getCurrencyTypeSender(), expectedTransaction.getCurrencyTypeSender());
        assertEquals(responseEntity.getCurrencyTypeRecipient(), expectedTransaction.getCurrencyTypeRecipient());
    }

    @Test
    void createTransactionAccountNotFoundException() {
        var account = new Account(UUID.randomUUID(), "12345678910111213111", Currency.RUR, BigDecimal.valueOf(10000), false, new Client());
        var account1 = new Account(UUID.randomUUID(), "12345678910111213112", Currency.RUR, BigDecimal.valueOf(10000), false, new Client());

        var transactionDTO = new TransactionDTO("12345678910111213111", "12345678910111213112", new BigDecimal(50));
        doReturn(Optional.empty()).when(accountService).findAccountByAccountNumber("12345678910111213111");
        assertThrows(AccountNotFoundException.class, () -> {
            transactionService.createTransaction(transactionDTO);
        });
        doReturn(Optional.of(account)).when(accountService).findAccountByAccountNumber("12345678910111213111");
        doReturn(Optional.empty()).when(accountService).findAccountByAccountNumber("12345678910111213112");
        assertThrows(AccountNotFoundException.class, () -> {
            transactionService.createTransaction(transactionDTO);
        });
    }

    @Test
    void createTransactionTransactionAmountException() {
        var account = new Account(UUID.randomUUID(), "12345678910111213111", Currency.RUR, BigDecimal.valueOf(0), false, new Client());
        var account1 = new Account(UUID.randomUUID(), "12345678910111213112", Currency.RUR, BigDecimal.valueOf(10000), false, new Client());

        var transactionDTO = new TransactionDTO("12345678910111213111", "12345678910111213112", new BigDecimal(50));
        var transactionDTO1 = new TransactionDTO("12345678910111213112", "12345678910111213111", new BigDecimal(500000));

        doReturn(Optional.of(account)).when(accountService).findAccountByAccountNumber("12345678910111213111");
        doReturn(Optional.of(account1)).when(accountService).findAccountByAccountNumber("12345678910111213112");
        assertThrows(TransactionAmountException.class, () -> {
            transactionService.createTransaction(transactionDTO);
        });
        assertThrows(TransactionAmountException.class, () -> {
            transactionService.createTransaction(transactionDTO1);
        });
    }

    @Test
    void createTransactionAccountIsBlockedException() {
        var account = new Account(UUID.randomUUID(), "12345678910111213111", Currency.RUR, BigDecimal.valueOf(10000), true, new Client());
        var account1 = new Account(UUID.randomUUID(), "12345678910111213112", Currency.RUR, BigDecimal.valueOf(10000), false, new Client());

        var transactionDTO = new TransactionDTO("12345678910111213111", "12345678910111213112", new BigDecimal(50));
        var transactionDTO1 = new TransactionDTO("12345678910111213112", "12345678910111213111", new BigDecimal(50));

        doReturn(Optional.of(account)).when(accountService).findAccountByAccountNumber("12345678910111213111");
        doReturn(Optional.of(account1)).when(accountService).findAccountByAccountNumber("12345678910111213112");
        assertThrows(AccountIsBlockedException.class, () -> {
            transactionService.createTransaction(transactionDTO);
        });
        assertThrows(AccountIsBlockedException.class, () -> {
            transactionService.createTransaction(transactionDTO1);
        });
    }

    @Test
    void createTransactionTransactionValidationException() {
        var account = new Account(UUID.randomUUID(), "12345678910111213111", Currency.RUR, BigDecimal.valueOf(10000), false, new Client());
        var account1 = new Account(UUID.randomUUID(), "12345678910111213112", Currency.RUR, BigDecimal.valueOf(10000), false, new Client());

        var transactionDTO = new TransactionDTO("12345678910111213111", "12345678910111213112", new BigDecimal(50));
        var expectedTransaction = new Transaction(UUID.randomUUID(), "12345678910111213111", "12345678910111213112", BigDecimal.valueOf(50), Currency.RUR, Currency.RUR, LocalDateTime.now(), LocalDateTime.now(), TransactionStatus.START);

        doReturn(Optional.of(account)).when(accountService).findAccountByAccountNumber(account.getAccountNumber());
        doReturn(Optional.of(account1)).when(accountService).findAccountByAccountNumber(account1.getAccountNumber());
        doReturn(Optional.of(expectedTransaction)).when(transactionRepository).findTransactionById(any(UUID.class));
        doReturn(expectedTransaction).when(transactionRepository).save(any(Transaction.class));

        assertThrows(TransactionValidationException.class, () -> {
            transactionService.createTransaction(transactionDTO);
        });
        verify(transactionRepository, times(0)).save(any(Transaction.class));
    }

    @Test
    void doTransaction() {
        var account = new Account(UUID.randomUUID(), "12345678910111213111", Currency.RUR, BigDecimal.valueOf(10000), false, new Client());
        var account1 = new Account(UUID.randomUUID(), "12345678910111213112", Currency.RUR, BigDecimal.valueOf(10000), false, new Client());
        var transaction = new Transaction(UUID.randomUUID(), "12345678910111213111", "12345678910111213112", BigDecimal.valueOf(50), Currency.RUR, Currency.RUR, LocalDateTime.now(), LocalDateTime.now(), TransactionStatus.PROCESSED);

        doReturn(Optional.of(account)).when(accountService).findAccountByAccountNumber(account.getAccountNumber());
        doReturn(Optional.of(account1)).when(accountService).findAccountByAccountNumber(account1.getAccountNumber());

        transactionService.doTransaction(transaction);

        assertEquals(transaction.getTransactionStatus(), TransactionStatus.ACCEPTED);
        assertEquals(account.getAmount(), BigDecimal.valueOf(9950));
        assertEquals(account1.getAmount(), BigDecimal.valueOf(10050));
    }

    @Test
    void doTransactionDifferentCurrency() {
        var account = new Account(UUID.randomUUID(), "12345678910111213111", Currency.RUR, BigDecimal.valueOf(10000), false, new Client());
        var account1 = new Account(UUID.randomUUID(), "12345678910111213112", Currency.USD, BigDecimal.valueOf(10000), false, new Client());
        var transaction = new Transaction(UUID.randomUUID(), "12345678910111213111", "12345678910111213112", BigDecimal.valueOf(50), Currency.RUR, Currency.USD, LocalDateTime.now(), LocalDateTime.now(), TransactionStatus.PROCESSED);

        doReturn(Optional.of(account)).when(accountService).findAccountByAccountNumber(account.getAccountNumber());
        doReturn(Optional.of(account1)).when(accountService).findAccountByAccountNumber(account1.getAccountNumber());
        doReturn(BigDecimal.valueOf(0.5)).when(currencyRateService).getRate(Currency.RUR, Currency.USD);

        transactionService.doTransaction(transaction);

        assertEquals(transaction.getTransactionStatus(), TransactionStatus.ACCEPTED);
        assertEquals(account.getAmount(), BigDecimal.valueOf(9950));
        assertEquals(account1.getAmount(), BigDecimal.valueOf(10025.0));
    }

    @Test
    void approveTransactionValidation() {
        var transactionStatusDTO = new TransactionStatusDTO("ACCEPTED");
        var transaction = new Transaction(UUID.randomUUID(), "12345678910111213111", "12345678910111213112", BigDecimal.valueOf(50), Currency.RUR, Currency.RUR, LocalDateTime.now(), LocalDateTime.now(), TransactionStatus.PROCESSED);

        doReturn(Optional.of(transaction)).when(transactionRepository).findTransactionById(transaction.getId());

        var responseEntity = transactionService.approveTransactionValidation(transactionStatusDTO, transaction.getId());

        assertNotNull(responseEntity);
        assertEquals(responseEntity.getId(), transaction.getId());
    }

    @Test
    void approveTransactionValidationTransactionStatusNotFoundException() {
        var transactionStatusDTO = new TransactionStatusDTO("QWERTY");
        var transaction = new Transaction(UUID.randomUUID(), "12345678910111213111", "12345678910111213112", BigDecimal.valueOf(50), Currency.RUR, Currency.RUR, LocalDateTime.now(), LocalDateTime.now(), TransactionStatus.PROCESSED);

        assertThrows(TransactionStatusNotFoundException.class, () -> {
            transactionService.approveTransactionValidation(transactionStatusDTO, transaction.getId());
        });
    }

    @Test
    void approveTransactionValidationTransactionNotFoundException() {
        var transactionStatusDTO = new TransactionStatusDTO("ACCEPTED");
        var transaction = new Transaction(UUID.randomUUID(), "12345678910111213111", "12345678910111213112", BigDecimal.valueOf(50), Currency.RUR, Currency.RUR, LocalDateTime.now(), LocalDateTime.now(), TransactionStatus.PROCESSED);
        doReturn(Optional.empty()).when(transactionRepository).findTransactionById(transaction.getId());

        assertThrows(TransactionNotFoundException.class, () -> {
            transactionService.approveTransactionValidation(transactionStatusDTO, transaction.getId());
        });
    }

    @Test
    void approveTransactionValidationTransactionException() {
        var transactionStatusDTO = new TransactionStatusDTO("ACCEPTED");
        var transaction = new Transaction(UUID.randomUUID(), "12345678910111213111", "12345678910111213112", BigDecimal.valueOf(50), Currency.RUR, Currency.RUR, LocalDateTime.now(), LocalDateTime.now(), TransactionStatus.START);

        doReturn(Optional.of(transaction)).when(transactionRepository).findTransactionById(transaction.getId());

        assertThrows(TransactionException.class, () -> {
            transactionService.approveTransactionValidation(transactionStatusDTO, transaction.getId());
        });
    }
}