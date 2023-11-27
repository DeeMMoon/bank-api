package com.sbt.bank.api.services;

import com.sbt.bank.api.models.Currency;

import java.math.BigDecimal;

/**
 * Интерфейс сервиса для работы с курсами валют
 *
 * @author Иванцов Дмитрий
 * @version 1.0
 * @see Currency
 */
public interface ICurrencyRateService {
    /**
     * Метод получения курса валют для конвертации из одной валюты в другую
     *
     * @param currencySender    {@link Currency Валюта} отправителя
     * @param currencyRecipient {@link Currency Валюта} получателя
     * @return Значение текущего курса типа {@link BigDecimal}
     */
    BigDecimal getRate(Currency currencySender, Currency currencyRecipient);
}
