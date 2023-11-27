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
import com.sbt.bank.api.models.Transaction;
import com.sbt.bank.api.models.TransactionStatus;
import com.sbt.bank.api.repositories.TransactionRepository;
import com.sbt.bank.api.services.IAccountService;
import com.sbt.bank.api.services.ICurrencyRateService;
import com.sbt.bank.api.services.ITransactionService;
import org.apache.commons.lang3.EnumUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

/**
 * Класс реализации сервиса по работе с транзакциями
 * (переводами средств между клиентами и подтверждением от сотрудника банка)
 *
 * @author Иванцов Дмитрий
 * @version 1.0
 * @see Transaction
 * @see TransactionStatus
 * @see TransactionDTO
 * @see TransactionRepository
 * @see IAccountService
 * @see ICurrencyRateService
 * @see Account
 */
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

    /**
     * Метод для изменения статуса транзакции при переводе суммы между клиентами.
     * При передаче того же статуса, что и у транзакции сейчас, статус изменен не будет.
     * При передаче иного статуса, статус и время последней модификации транзакции будут обновлены.
     *
     * @param status      {@link TransactionStatus Статус} на который нужно поменять текущий статус транзакции
     * @param transaction {@link Transaction Транзакция} в которой нужно обновить статус
     * @return {@link Transaction Транзакция} c обновленным статусом в {@link Optional} формате
     */
    @Override
    @Transactional
    public Optional<Transaction> updateTransactionStatus(TransactionStatus status, Transaction transaction) {
        if (transaction.getTransactionStatus() == status) {
            return Optional.of(transaction);
        }
        transaction.setUpdatedStatusTime(LocalDateTime.now());
        transaction.setTransactionStatus(status);
        transactionRepository.save(transaction);
        return Optional.of(transaction);
    }

    /**
     * Метод для создания новой транзакции.
     * Перед созданием транзакции, происходит валидация поступающих значений. После этого создается транзакция
     * и происходит проверка, что транзакции с таким же id уже не существует, в противном случае будет выброшено исключение
     * с кодом ответа 500
     *
     * @param transactionDTO {@link TransactionDTO DTO класс} с информацией о {@link Account#getAccountNumber() номере счета}
     *                       отправителя и получателя, а также сумме перевода
     * @return Новая {@link Transaction транзакция}
     */
    @Override
    @Transactional
    public Transaction createTransaction(TransactionDTO transactionDTO) {
        var accountSender = accountService.findAccountByAccountNumber(transactionDTO.accountNumberSender());
        var accountRecipient = accountService.findAccountByAccountNumber(transactionDTO.accountNumberRecipient());
        beforeTransactionValidation(transactionDTO, accountSender, accountRecipient);
        Transaction transaction = initTransaction(transactionDTO.amount(), accountSender.get(), accountRecipient.get());
        if (transactionRepository.findTransactionById(transaction.getId()).isPresent()) {
            throw new TransactionValidationException("Transaction with id = " + transaction.getId() + "already exist");
        }
        return transactionRepository.save(transaction);
    }

    /**
     * Метод выполнения транзакции.
     * Перед выполнением транзакции, происходит валидация поступающих значений.
     * После этого происходит списание средств со счёта отправителя, конвертация в другую валюту (если валюты счетов разные),
     * зачисление средств на счёт получателя и обновление статуса транзакции как "ACCEPTED"
     *
     * @param transaction {@link Transaction Транзакция} в которой хранится все информация о переводе
     */
    @Override
    @Transactional
    public void doTransaction(Transaction transaction) {
        var accountSender = accountService.findAccountByAccountNumber(transaction.getAccountNumberSender());
        var accountRecipient = accountService.findAccountByAccountNumber(transaction.getAccountNumberRecipient());
        beforeTransactionValidation(TransactionDTO.map(transaction), accountSender, accountRecipient);
        accountSender.get().setAmount(accountSender.get().getAmount().subtract(transaction.getAmount()));
        BigDecimal convertedAmount = conversionCurrency(accountSender.get(), accountRecipient.get()).multiply(transaction.getAmount());
        accountRecipient.get().setAmount(accountRecipient.get().getAmount().add(convertedAmount));
        updateTransactionStatus(TransactionStatus.ACCEPTED, transaction);
    }

    /**
     * Метод создания транзакции.
     * Сумма перевода умножается на 100 для приведения ее в разменную денежную единицу.
     * Первоначальный статус транзакции - "START"
     *
     * @param amount           Сумма перевода
     * @param accountSender    {@link Account Счёт} отправителя
     * @param accountRecipient {@link Account Счёт} получателя
     * @return Новая заполненная транзакция
     */
    private Transaction initTransaction(BigDecimal amount, Account accountSender, Account accountRecipient) {
        Transaction transaction = new Transaction().builder()
                .amount(amount.multiply(new BigDecimal(100)))
                .accountNumberSender(accountSender.getAccountNumber())
                .accountNumberRecipient(accountRecipient.getAccountNumber())
                .id(UUID.randomUUID())
                .transactionStatus(TransactionStatus.START)
                .sendingTime(LocalDateTime.now())
                .updatedStatusTime(LocalDateTime.now())
                .currencyTypeSender(accountSender.getCurrency())
                .currencyTypeRecipient(accountRecipient.getCurrency())
                .build();
        return transaction;
    }

    /**
     * Функция валидации данных транзакции.
     * Проверки 1 и 2: счёта получателя и отправителя существуют, иначе будет выброшено исключение с кодом ответа 404;
     * Проверка 3: баланс отправителя больше нуля и сумма, которую он хочет отправить меньше, чем его баланс, иначе будет
     * выброшено исключение с кодом ответа 404 (checkAmount умножается на 100 для перевода суммы в ее в разменную денежную единицу);
     * Проверки 4 и 5: счёта получателя и отправителя не заблокированные, иначе будет выброшено исключение с кодом ответа 404.
     *
     * @param transaction      {@link TransactionDTO DTO класс} содержащий информацию по осуществлению перевода
     * @param accountSender    {@link Account Счёт} отправителя
     * @param accountRecipient {@link Account Счёт} получателя
     */
    private void beforeTransactionValidation(TransactionDTO transaction, Optional<Account> accountSender, Optional<Account> accountRecipient) {
        if (accountSender.isEmpty()) {
            throw new AccountNotFoundException("Can't create transfer because account with number: "
                    + transaction.accountNumberSender() + " not found");
        }
        if (accountRecipient.isEmpty()) {
            throw new AccountNotFoundException("Can't create transfer because account with number: "
                    + transaction.accountNumberRecipient() + " not found");
        }
        var checkAmount = transaction.amount().multiply(BigDecimal.valueOf(100.00));
        if (checkAmount.compareTo(BigDecimal.ZERO) <= 0
                || accountSender.get().getAmount().subtract(checkAmount).compareTo(BigDecimal.ZERO) < 0) {
            throw new TransactionAmountException("Amount should be more zero and sender's balance more then amount");
        }
        if (accountSender.get().getIsBlocked()) {
            throw new AccountIsBlockedException("This account is blocked, you can't do transfer");
        }
        if (accountRecipient.get().getIsBlocked()) {
            throw new AccountIsBlockedException("Recipient account is blocked, you can't do transfer");
        }
    }

    /**
     * Функция конвертации валют.
     * Если валюта одинаковая, то возвращается коэффициент равный 1,
     * иначе собирается ключ из двух валют и передается в функцию получения коэффициента.
     *
     * @param accountSender    {@link Account Счёт} отправителя
     * @param accountRecipient {@link Account Счёт} получателя
     * @return
     */
    private BigDecimal conversionCurrency(Account accountSender, Account accountRecipient) {
        var senderCurrency = accountSender.getCurrency();
        var recipientCurrency = accountRecipient.getCurrency();
        if (senderCurrency == recipientCurrency) {
            return BigDecimal.ONE;
        }
        return currencyRateService.getRate(senderCurrency, recipientCurrency);
    }

    /**
     * Метод валидации транзакции при ее подтверждении/отклонении сотрудником банка.
     * Проверка 1: переданный статус соответствует существующим статусам, иначе будет выброшено исключение с кодом ошибки 404
     * Проверка 2: транзакция с переданным id существует, иначе будет выброшено исключение с кодом ошибки 404
     *
     * @param transactionStatusDTO {@link TransactionStatusDTO DTO класс} хранящий статус изменения транзакции
     * @param id                   Уникальный идентификатор транзакции типа {@link UUID}
     * @return Корректная {@link Transaction транзакция} с переданным id
     */
    @Override
    public Transaction approveTransactionValidation(TransactionStatusDTO transactionStatusDTO, UUID id) {
        if (!EnumUtils.isValidEnum(TransactionStatus.class, transactionStatusDTO.status().toUpperCase())) {
            throw new TransactionStatusNotFoundException("Transaction status with value: " + transactionStatusDTO.status() + " not found");
        }
        var transaction = transactionRepository.findTransactionById(id);
        if (transaction.isEmpty()) {
            throw new TransactionNotFoundException("Transaction with id: " + id + " not found");
        }
        if (!transaction.get().getTransactionStatus().equals(TransactionStatus.PROCESSED)) {
            throw new TransactionException("The transaction status cannot be changed because it was changed earlier");
        }
        return transaction.get();
    }
}
