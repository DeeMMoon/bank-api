package com.sbt.bank.api.services.impl;

import com.sbt.bank.api.exceptions.account.AccountNotFoundException;
import com.sbt.bank.api.exceptions.account.TooLongTimeGenerationAccountNumberException;
import com.sbt.bank.api.models.Account;
import com.sbt.bank.api.models.Client;
import com.sbt.bank.api.models.Currency;
import com.sbt.bank.api.repositories.AccountRepository;
import com.sbt.bank.api.services.IAccountService;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Класс реализации сервиса по работе со счетами клиентов
 *
 * @author Иванцов Дмитрий
 * @version 1.0
 * @see AccountRepository
 * @see IAccountService
 * @see Account
 * @see Client
 * @see Currency
 */
@Service
@Transactional(readOnly = true)
public class AccountServiceImpl implements IAccountService {

    private final AccountRepository accountRepository;

    public AccountServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    /**
     * Метод добавления добавление суммы на счёт (зачисление происходит в разменной денежной единице)
     * При пердаче пустого пользователя произойдет выбрасывание {@link AccountNotFoundException иссключения},
     * которое вернет 404 статус ответа. При положительном исходе сумма будет начислена и сохранена в базе данных.
     *
     * @param account Счёт куда будет происходить зачисление средств
     * @param amount  Сумма, которая будет зачислена
     * @return {@link Account Счёт} с обновленным балансом
     */
    @Override
    @Transactional
    public Account addDepositToTheAccountBalance(Optional<Account> account, BigDecimal amount) {
        if (account.isEmpty()) {
            throw new AccountNotFoundException("Can't get amount, because account not found");
        }
        account.get().setAmount(account.get().getAmount().add(amount));
        accountRepository.save(account.get());
        return account.get();
    }

    /**
     * Метод поиска счёта с переданным номером среди всех счетов
     *
     * @param accountNumber Уникальное значение каждого счета состоящего из 20 цифр {@link Account#getAccountNumber()}
     * @return {@link Account Счёт} с переданным {@link Account#getAccountNumber() номером счета} в {@link Optional} формате
     */
    @Override
    public Optional<Account> findAccountByAccountNumber(String accountNumber) {
        return accountRepository.findAccountByAccountNumber(accountNumber);
    }

    /**
     * Метод поиска счёта с переданным номером среди переданных счетов.
     * Если счёта с переданным номером не будет, то будет выброшено исключение со статусом ответа 404.
     *
     * @param accountNumber Уникальное значение каждого счета состоящего из 20 цифр {@link Account#getAccountNumber()}
     * @param accounts      Список счётов среди которых происходит поиск {@link List<Account>}
     * @return
     */
    @Override
    public Account findAccountByAccountNumber(String accountNumber, List<Account> accounts) {
        return accounts.stream()
                .filter(p -> p.getAccountNumber().equals(accountNumber))
                .findAny()
                .orElseThrow(() -> new AccountNotFoundException("Account with number: " + accountNumber + " not found"));
    }

    /**
     * Метод создания счёта.
     * При создании счёта происходит генерация его уникального номера состоящего из 20 цифр,
     * если этот номер создается более 1 минуты, то выбрасывается исключение со статусом ответа 500.
     * По умолчанию счёт не заблокирован и сумма на балансе равна 0.
     *
     * @param client   Клиент {@link Client} у которого будет создан новый счет
     * @param currency Валюта счёта {@link Currency}
     * @return Созданный счёт
     * @see AccountServiceImpl#accountNumberGenerator()
     * @see AccountServiceImpl#checkGeneratedAccountNumberIsUnique(String)
     */
    @Transactional
    @Override
    public Account createAccount(Client client, Currency currency) {
        String accountNumber = accountNumberGenerator();
        LocalDateTime endTimeGenerationAccountNumber = LocalDateTime.now().plusMinutes(1);
        while (!checkGeneratedAccountNumberIsUnique(accountNumber)) {
            accountNumber = accountNumberGenerator();
            if (LocalDateTime.now().isAfter(endTimeGenerationAccountNumber)) {
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

    /**
     * Функция генерации номера счёта, состоящего из 20 цифр
     *
     * @return Номер счёта
     */
    private String accountNumberGenerator() {
        return RandomStringUtils.randomNumeric(20);
    }

    /**
     * Функция проверки номера счёта на его уникальность.
     *
     * @param accountNumber Уникальный номер счета состоящий из 20 цифр
     * @return true если номер счета уникальный, false если нет
     */
    private Boolean checkGeneratedAccountNumberIsUnique(String accountNumber) {
        return accountRepository.findAccountByAccountNumber(accountNumber).isEmpty();
    }

}
