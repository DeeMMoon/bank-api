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
import lombok.NonNull;
import lombok.Setter;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Getter
@Setter
@Embeddable
public class CurrencyRateKey implements Serializable {

    @NotNull(message = "Converter currency must not be null")
    @Enumerated(EnumType.STRING)
    @Column(name = "converter_currency")
    private Currency converterCurrency;

    @NotNull(message = "Converted currency must not be null")
    @Enumerated(EnumType.STRING)
    @Column(name = "converted_currency")
    private Currency convertedCurrency;
}
