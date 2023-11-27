package com.sbt.bank.api.dto;

import com.sbt.bank.api.models.Account;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;

import java.math.BigDecimal;

/**
 * Record запроса с информацией о {@link Account#accountNumber номере счета} в {@link String} формате
 * и суммы зачисления на {@link Account счёт} в формате разменной денежной единице
 *
 * @author Иванцов Дмитрий
 * @version 1.0
 * @see Account
 */
@Schema(description = "Запрос на зачисление суммы на банковский счёт")
@Builder
public record AccountRequest(@Pattern(message = "Incorrect account number", regexp = "^[0-9]{20}$")
                             @NotBlank(message = "The account number must not be blank")
                             @Schema(description = "Номер банковского счета", example = "12345678910111213150")
                             String accountNumber,
                             @NotNull(message = "Amount must not be null")
                             @DecimalMin(value = "0", inclusive = false, message = "Amount must be more 0")
                             @Schema(description = "Сумма перевода(в формате разменной денежной единице)", example = "10000")
                             BigDecimal amount) {
}
