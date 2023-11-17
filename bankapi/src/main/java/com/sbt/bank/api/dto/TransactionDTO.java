package com.sbt.bank.api.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.math.BigDecimal;

public record TransactionDTO(@Pattern(message = "Incorrect account number", regexp = "^[0-9]{20}$")
                             @NotNull
                             String accountNumberSender,
                             @Pattern(message = "Incorrect account number", regexp = "^[0-9]{20}$")
                             @NotNull
                             String accountNumberRecipient,
                             @DecimalMin(value = "0.01", inclusive = true, message = "Amount must be more 0.00")
                             @DecimalMax(value = "1000000.00", inclusive = false, message = "Amount must be less than 1 000 000.00")
                             @NotNull
                             @Digits(integer=6, fraction=2, message = "Incorrect amount input")
                             BigDecimal amount) {}
