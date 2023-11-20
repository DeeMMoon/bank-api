package com.sbt.bank.api.models;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Entity
@Table(name = "currency_rate")
public class CurrencyRate {

    @EmbeddedId
    private CurrencyRateKey id;

    @Min(value = 0)
    @NotNull(message = "Last modified time must not be null")
    private BigDecimal rate;
}
