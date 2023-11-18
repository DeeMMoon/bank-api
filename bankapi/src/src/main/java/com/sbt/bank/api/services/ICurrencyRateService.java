package com.sbt.bank.api.services;

import com.sbt.bank.api.models.Currency;
import com.sbt.bank.api.models.CurrencyRate;
import com.sbt.bank.api.models.CurrencyRateKey;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

public interface ICurrencyRateService {
    Optional<CurrencyRate> findCurrencyRateById(CurrencyRateKey key);
    BigDecimal getRate(Currency currencySender, Currency currencyRecipient);
}
