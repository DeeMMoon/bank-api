package com.sbt.bank.api.models;

/**
 * Enum статусов, которые могут быть у транзакции
 * <p>
 * * @author Иванцов Дмитрий
 * * @version 1.0
 */
public enum TransactionStatus {
    /**
     * Транзакция была создана
     */
    START,
    /**
     * Транзакция находится в режиме ожидания
     */
    PROCESSED,
    /**
     * Транзакция отклонена
     */
    REJECTED,
    /**
     * Транзакция принята
     */
    ACCEPTED,
    /**
     * Во время принятия транзакции что-то пошло не так
     */
    FAIL
}
