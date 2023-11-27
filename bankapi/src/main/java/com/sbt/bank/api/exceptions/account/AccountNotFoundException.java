package com.sbt.bank.api.exceptions.account;

public final class AccountNotFoundException extends AccountException {
    public AccountNotFoundException(String message) {
        super(message);
    }
}
