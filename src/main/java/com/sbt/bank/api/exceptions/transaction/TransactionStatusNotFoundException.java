package com.sbt.bank.api.exceptions.transaction;

public final class TransactionStatusNotFoundException extends TransactionException {

    public TransactionStatusNotFoundException(String message) {
        super(message);
    }
}
