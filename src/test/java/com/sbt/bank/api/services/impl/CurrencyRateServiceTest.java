package com.sbt.bank.api.services.impl;

import com.sbt.bank.api.exceptions.currency.rate.CurrencyRateNotFoundException;
import com.sbt.bank.api.models.Currency;
import com.sbt.bank.api.models.CurrencyRate;
import com.sbt.bank.api.models.CurrencyRateKey;
import com.sbt.bank.api.repositories.CurrencyRateRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CurrencyRateServiceTest {

    @Mock
    private CurrencyRateRepository currencyRateRepository;

    @InjectMocks
    private CurrencyRateService currencyRateService;

    @AfterEach
    public void resetMocks() {
        reset(currencyRateRepository);
    }

    @Test
    void getRate() {
        var key = new CurrencyRateKey(Currency.RUR, Currency.USD);
        CurrencyRate currencyRate = new CurrencyRate(key, BigDecimal.valueOf(100));
        doReturn(Optional.of(currencyRate)).when(currencyRateRepository).findCurrencyRateById(key);

        var responseEntity = currencyRateService.getRate(Currency.RUR, Currency.USD);

        assertNotNull(responseEntity);
        assertEquals(responseEntity, BigDecimal.valueOf(100));
    }

    @Test
    void getRateCurrencyRateNotFoundException() {
        doReturn(Optional.empty()).when(currencyRateRepository).findCurrencyRateById(any(CurrencyRateKey.class));
        assertThrows(CurrencyRateNotFoundException.class, () -> {
            currencyRateService.getRate(Currency.RUR, Currency.USD);
        });
        verify(currencyRateRepository, times(1)).findCurrencyRateById(any(CurrencyRateKey.class));

    }
}