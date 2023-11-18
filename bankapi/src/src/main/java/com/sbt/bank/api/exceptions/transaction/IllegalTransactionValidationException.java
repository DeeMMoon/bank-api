package com.sbt.bank.api.exceptions.transaction;

public final class IllegalTransactionValidationException extends IllegalTransactionException {
    public IllegalTransactionValidationException(String message) {
        super(message);
    }
}
