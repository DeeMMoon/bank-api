package com.sbt.bank.api.services;

import com.sbt.bank.api.dto.TransactionDTO;
import com.sbt.bank.api.dto.TransactionStatusDTO;
import com.sbt.bank.api.models.Account;
import com.sbt.bank.api.models.Transaction;
import com.sbt.bank.api.models.TransactionStatus;

import java.util.Optional;
import java.util.UUID;

/**
 * Интерфейс сервиса по работе с транзакциями
 * (переводами средств между клиентами и подтверждением от сотрудника банка)
 *
 * @author Иванцов Дмитрий
 * @version 1.0
 * @see Transaction
 * @see TransactionStatus
 * @see TransactionDTO
 */
public interface ITransactionService {
    /**
     * Метод для изменения статуса транзакции при переводе суммы между клиентами
     *
     * @param status      {@link TransactionStatus Статус} на который нужно поменять текущий статус транзакции
     * @param transaction {@link Transaction Транзакция} в которой нужно обновить статус
     * @return {@link Transaction Транзакция} c обновленным статусом в {@link Optional} формате
     */
    Optional<Transaction> updateTransactionStatus(TransactionStatus status, Transaction transaction);

    /**
     * Метод для создания новой транзакции
     *
     * @param transactionDTO {@link TransactionDTO DTO класс} с информацией о {@link Account#getAccountNumber() номере счета}
     *                       отправителя и получателя, а также сумме перевода
     * @return Новая {@link Transaction транзакция}
     */
    Transaction createTransaction(TransactionDTO transactionDTO);

    /**
     * Метод для выполнения транзакции (снятие средств, конвертация в другую валюту (если они разные) зачисление средств)
     *
     * @param transaction {@link Transaction Транзакция} в которой хранится все информация о переводе
     */
    void doTransaction(Transaction transaction);

    /**
     * Валидационный метод транзакции, который должен выполняться перед вызовом метода исполнения транзакции.
     * Проверяет корректность переданных параметров.
     *
     * @param transactionStatusDTO {@link TransactionStatusDTO DTO класс} хранящий статус изменения транзакции
     * @param id                   Уникальный идентификатор транзакции типа {@link UUID}
     * @return Корректная {@link Transaction транзакция} с переданным id
     */
    Transaction approveTransactionValidation(TransactionStatusDTO transactionStatusDTO, UUID id);
}
