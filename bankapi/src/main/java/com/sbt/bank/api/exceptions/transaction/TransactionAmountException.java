package com.sbt.bank.api.exceptions.transaction;

public final class TransactionAmountException extends TransactionException {
    public TransactionAmountException(String message) {
        super(message);
    }
}
