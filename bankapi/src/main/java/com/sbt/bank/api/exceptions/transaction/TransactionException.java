package com.sbt.bank.api.exceptions.transaction;

/**
 * Родительский класс исключений связанных с {@link com.sbt.bank.api.models.Transaction транзакциями}
 *
 * @author Иванцов Дмитрий
 * @version 1.0
 */
public class TransactionException extends RuntimeException {
    public TransactionException(String message) {
        super(message);
    }
}
