package com.sbt.bank.api.exceptions.transaction;

public final class TransactionValidationException extends TransactionException {
    public TransactionValidationException(String message) {
        super(message);
    }
}
