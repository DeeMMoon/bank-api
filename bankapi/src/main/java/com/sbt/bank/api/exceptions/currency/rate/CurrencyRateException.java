package com.sbt.bank.api.exceptions.currency.rate;

import com.sbt.bank.api.exceptions.currency.CurrencyException;

/**
 * Родительский класс исключений связанных с {@link com.sbt.bank.api.models.CurrencyRate конвертацией валют}
 *
 * @author Иванцов Дмитрий
 * @version 1.0
 */
public class CurrencyRateException extends CurrencyException {
    public CurrencyRateException(String message) {
        super(message);
    }
}
