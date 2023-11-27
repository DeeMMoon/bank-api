package com.sbt.bank.api.exceptions.account;

public final class TooLongTimeGenerationAccountNumberException extends AccountException {
    public TooLongTimeGenerationAccountNumberException(String message) {
        super(message);
    }
}
