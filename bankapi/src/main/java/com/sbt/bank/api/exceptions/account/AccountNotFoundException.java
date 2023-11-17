package com.sbt.bank.api.exceptions.account;

public final class AccountNotFoundException extends RuntimeException{
    public AccountNotFoundException(String message) {
        super(message);
    }
}
