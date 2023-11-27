package com.sbt.bank.api.models;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * Составной ключ для таблицы CurrencyRate
 *
 * @author Иванцов Дмитрий
 * @version 1.0
 * @see Currency
 */
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Getter
@Setter
@Embeddable
public class CurrencyRateKey implements Serializable {

    /**
     * {@link Currency Валюта} из которой будет осуществляться конвертация
     */
    @NotNull(message = "Converter currency must not be null")
    @Enumerated(EnumType.STRING)
    @Column(name = "converter_currency")
    private Currency converterCurrency;

    /**
     * {@link Currency Валюта} в которую будет осуществляться конвертация
     */
    @NotNull(message = "Converted currency must not be null")
    @Enumerated(EnumType.STRING)
    @Column(name = "converted_currency")
    private Currency convertedCurrency;
}
