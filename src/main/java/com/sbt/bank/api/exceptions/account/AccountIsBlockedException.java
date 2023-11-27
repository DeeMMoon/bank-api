package com.sbt.bank.api.exceptions.account;

public final class AccountIsBlockedException extends AccountException {
    public AccountIsBlockedException(String message) {
        super(message);
    }
}
