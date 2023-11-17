package com.sbt.bank.api.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import java.math.BigDecimal;

public record AccountRequestDTO (@Pattern(message = "Incorrect account number", regexp = "^[0-9]{20}$")
                       @NotBlank String accountNumber,
                       @NotNull
                       @DecimalMin(value = "0", inclusive = false, message = "Amount must be more 0")
                       BigDecimal amount){}
