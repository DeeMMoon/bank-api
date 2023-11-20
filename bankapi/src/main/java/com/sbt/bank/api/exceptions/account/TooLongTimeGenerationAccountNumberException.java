package com.sbt.bank.api.exceptions.account;

public class TooLongTimeGenerationAccountNumberException extends AccountException{
    public TooLongTimeGenerationAccountNumberException(String message) {
        super(message);
    }
}
