package com.sbt.bank.api.models;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * Сущность курса перевода валют
 *
 * @author Иванцов Дмитрий
 * @version 1.0
 * @see CurrencyRateKey
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Entity
@Table(name = "currency_rate")
public class CurrencyRate {

    /**
     * Составной уникальный id (Например RURUSD)
     */
    @EmbeddedId
    private CurrencyRateKey id;

    /**
     * Курс конвертации валют
     */
    @DecimalMin(value = "0", inclusive = false, message = "Rate must be more 0")
    @NotNull(message = "Last modified time must not be null")
    private BigDecimal rate;
}
