package com.sbt.bank.api.exceptions.currency;

public final class CurrencyNotFoundException extends CurrencyException {
    public CurrencyNotFoundException(String message) {
        super(message);
    }
}
