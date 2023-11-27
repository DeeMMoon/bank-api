package com.sbt.bank.api.services;

import com.sbt.bank.api.models.Account;
import com.sbt.bank.api.models.Client;
import com.sbt.bank.api.models.Currency;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Интерфей для работы с счетами клиентов
 *
 * @author Иванцов Дмитрий
 * @version 1.0
 * @see Account
 * @see Currency
 * @see Client
 */
public interface IAccountService {
    /**
     * Метод зачисления переданной суммы на счёт
     *
     * @param account Счёт куда будет происходить зачисление средств
     * @param amount  Сумма, которая будет зачислена
     * @return {@link Account Счёт} куда были зачислены средства
     */
    Account addDepositToTheAccountBalance(Optional<Account> account, BigDecimal amount);

    /**
     * Метод поиска счёта с переданным номером среди всех счетов
     *
     * @param accountNumber Уникальное значение каждого счета состоящего из 20 цифр {@link Account#getAccountNumber()}
     * @return {@link Account Счёт} с переданным {@link Account#getAccountNumber() номером счета} в {@link Optional} формате
     * @see IAccountService#findAccountByAccountNumber(String, List)
     */
    Optional<Account> findAccountByAccountNumber(String accountNumber);

    /**
     * Метод поиска счёта с переданным номером среди переданных счетов
     *
     * @param accountNumber Уникальное значение каждого счета состоящего из 20 цифр {@link Account#getAccountNumber()}
     * @param accounts      Список счётов среди которых происходит поиск {@link List<Account>}
     * @return {@link Account Счёт} c переданным {@link Account#getAccountNumber() номером счета}
     * из переданного списка счетов
     * @see IAccountService#findAccountByAccountNumber(String)
     */

    Account findAccountByAccountNumber(String accountNumber, List<Account> accounts);

    /**
     * Метод для создания нового счёта у клиента
     *
     * @param client   Клиент {@link Client} у которого будет создан новый счет
     * @param currency Валюта счёта {@link Currency}
     * @return Новый счёт {@link Account}
     */
    Account createAccount(Client client, Currency currency);
}
