package com.sbt.bank.api.exceptions.transaction;

public final class IllegalTransactionAmountException extends IllegalTransactionException{
    public IllegalTransactionAmountException(String message) {
        super(message);
    }
}
