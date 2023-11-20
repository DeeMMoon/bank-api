package com.sbt.bank.api.exceptions.currency;

public class CurrencyNotFoundException extends CurrencyException{
    public CurrencyNotFoundException(String message) {
        super(message);
    }
}
