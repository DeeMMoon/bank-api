package com.sbt.bank.api.exceptions.transaction;

public final class TransactionNotFoundException extends IllegalTransactionException {

    public TransactionNotFoundException(String message) {
        super(message);
    }
}
