package com.sbt.bank.api.exceptions.account;

public class AccountIsBlockedException extends AccountException {
    public AccountIsBlockedException(String message) {
        super(message);
    }
}
