package com.sbt.bank.api.exceptions.account;

/**
 * Родительский класс исключений связанных с {@link com.sbt.bank.api.models.Account банковкими счётами}
 *
 * @author Иванцов Дмитрий
 * @version 1.0
 */
public class AccountException extends RuntimeException {
    public AccountException(String message) {
        super(message);
    }
}
