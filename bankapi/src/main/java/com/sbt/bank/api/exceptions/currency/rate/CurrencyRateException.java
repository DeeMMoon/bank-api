package com.sbt.bank.api.exceptions.currency.rate;

import com.sbt.bank.api.exceptions.currency.CurrencyException;

public class CurrencyRateException extends CurrencyException {
    public CurrencyRateException(String message) {
        super(message);
    }
}
