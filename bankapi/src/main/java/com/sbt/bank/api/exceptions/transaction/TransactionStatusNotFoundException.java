package com.sbt.bank.api.exceptions.transaction;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public final class TransactionStatusNotFoundException extends IllegalTransactionException {

    public TransactionStatusNotFoundException(String message) {
        super(message);
    }
}
