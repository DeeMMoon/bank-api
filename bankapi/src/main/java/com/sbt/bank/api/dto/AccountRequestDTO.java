package com.sbt.bank.api.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.springframework.beans.factory.annotation.Value;

import java.math.BigDecimal;

public record AccountRequestDTO(@Pattern(message = "Incorrect account number", regexp = "^[0-9]{20}$")
                                @NotBlank(message = "The account number must not be blank")
                                String accountNumber,
                                @NotNull(message = "Amount must not be null")
                                @DecimalMin(value = "0", inclusive = false, message = "Amount must be more 0")
                                BigDecimal amount) {
}
