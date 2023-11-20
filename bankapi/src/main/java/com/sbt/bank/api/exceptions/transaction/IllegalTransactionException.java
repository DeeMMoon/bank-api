package com.sbt.bank.api.exceptions.transaction;

public sealed class IllegalTransactionException extends RuntimeException permits IllegalTransactionAmountException, IllegalTransactionConversationCurrencyException, IllegalTransactionValidationException, TransactionNotFoundException, TransactionStatusNotFoundException {
    public IllegalTransactionException(String message) {
        super(message);
    }
}
