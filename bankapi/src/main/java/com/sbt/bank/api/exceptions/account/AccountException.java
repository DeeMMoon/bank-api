package com.sbt.bank.api.exceptions.account;

public class AccountException extends RuntimeException{
    public AccountException(String message) {
        super(message);
    }
}