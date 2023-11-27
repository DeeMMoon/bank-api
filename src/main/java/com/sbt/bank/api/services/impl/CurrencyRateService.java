package com.sbt.bank.api.services.impl;

import com.sbt.bank.api.exceptions.currency.rate.CurrencyRateNotFoundException;
import com.sbt.bank.api.models.Currency;
import com.sbt.bank.api.models.CurrencyRate;
import com.sbt.bank.api.models.CurrencyRateKey;
import com.sbt.bank.api.repositories.CurrencyRateRepository;
import com.sbt.bank.api.services.ICurrencyRateService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

/**
 * Класс реализации сервиса для работы с курсами валют
 *
 * @author Иванцов Дмитрий
 * @version 1.0
 * @see Currency
 * @see CurrencyRateRepository
 * @see CurrencyRateKey
 */
@Service
@Transactional(readOnly = true)
public class CurrencyRateService implements ICurrencyRateService {

    private final CurrencyRateRepository currencyRateRepository;

    public CurrencyRateService(CurrencyRateRepository currencyRateRepository) {
        this.currencyRateRepository = currencyRateRepository;
    }

    /**
     * Метод для получения курса перевода валют.
     * Создается составной ключ, состоящий из наименований двух валют,
     * после чего по данному ключу происходит поиск курса. Если значений с данным ключом
     * нет, то выбрасывается исключение с кодом ответа 404.
     *
     * @param currencySender    {@link Currency Валюта} отправителя
     * @param currencyRecipient {@link Currency Валюта} получателя
     * @return Значение текущего курса типа {@link BigDecimal}
     */
    @Override
    public BigDecimal getRate(Currency currencySender, Currency currencyRecipient) {
        Optional<CurrencyRate> currencyRate = currencyRateRepository.findCurrencyRateById(new CurrencyRateKey(currencySender, currencyRecipient));
        if (currencyRate.isEmpty()) {
            throw new CurrencyRateNotFoundException("The exchange rate for currencies was not found");
        }
        return currencyRate.get().getRate();
    }
}
