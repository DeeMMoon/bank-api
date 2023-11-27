package com.sbt.bank.api.exceptions.currency;

/**
 * Родительский класс исключений связанных с {@link com.sbt.bank.api.models.Currency валютами}
 *
 * @author Иванцов Дмитрий
 * @version 1.0
 */
public class CurrencyException extends RuntimeException {
    public CurrencyException(String message) {
        super(message);
    }
}
