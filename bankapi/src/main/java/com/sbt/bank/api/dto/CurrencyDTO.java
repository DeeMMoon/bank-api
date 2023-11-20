package com.sbt.bank.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CurrencyDTO(@NotBlank(message = "Currency must not be blank") @Size(min = 3, max = 4) String currency) {
}
