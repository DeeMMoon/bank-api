package com.sbt.bank.api.services;

import com.sbt.bank.api.exceptions.currency.rate.CurrencyRateNotFoundException;
import com.sbt.bank.api.models.Currency;
import com.sbt.bank.api.models.CurrencyRate;
import com.sbt.bank.api.models.CurrencyRateKey;
import com.sbt.bank.api.repositories.CurrencyRateRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class CurrencyRateService implements ICurrencyRateService {

    private final CurrencyRateRepository currencyRateRepository;

    public CurrencyRateService(CurrencyRateRepository currencyRateRepository) {
        this.currencyRateRepository = currencyRateRepository;
    }

    @Override
    public Optional<CurrencyRate> findCurrencyRateById(CurrencyRateKey key) {
        return currencyRateRepository.findCurrencyRateById(key);
    }

    @Override
    public BigDecimal getRate(Currency currencySender, Currency currencyRecipient) {
        CurrencyRateKey currencyRateKey = new CurrencyRateKey();
        currencyRateKey.setConverterCurrency(currencySender);
        currencyRateKey.setConvertedCurrency(currencyRecipient);
        Optional<CurrencyRate> currencyRate = findCurrencyRateById(currencyRateKey);
        if (currencyRate.isPresent()){
            return currencyRate.get().getRate();
        }
        throw new CurrencyRateNotFoundException("The exchange rate for currencies was not found");
    }
}
