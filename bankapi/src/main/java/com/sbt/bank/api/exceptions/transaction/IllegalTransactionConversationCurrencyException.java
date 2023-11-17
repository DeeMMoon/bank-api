package com.sbt.bank.api.exceptions.transaction;

public final class IllegalTransactionConversationCurrencyException extends IllegalTransactionException{
    public IllegalTransactionConversationCurrencyException(String message) {
        super(message);
    }
}
