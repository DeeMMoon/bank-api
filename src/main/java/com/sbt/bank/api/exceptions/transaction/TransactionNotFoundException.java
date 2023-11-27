package com.sbt.bank.api.exceptions.transaction;

public final class TransactionNotFoundException extends TransactionException {

    public TransactionNotFoundException(String message) {
        super(message);
    }
}
