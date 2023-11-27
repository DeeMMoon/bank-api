package com.sbt.bank.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Record DTO валюты с полем <b>currency</b>
 *
 * @author Иванцов Дмитрий
 * @version 1.0
 * @see com.sbt.bank.api.models.Currency
 */
@Schema(description = "Сущность валюты")
public record CurrencyDTO(@NotBlank(message = "Currency must not be blank")
                          @Size(min = 3, max = 4)
                          @Schema(description = "Валюта", example = "RUR")
                          String currency) {
}
