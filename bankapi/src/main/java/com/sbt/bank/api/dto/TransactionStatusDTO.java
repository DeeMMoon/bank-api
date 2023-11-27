package com.sbt.bank.api.dto;

import com.sbt.bank.api.models.TransactionStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Record DTO статуса транзакции с полем <b>status</b>
 *
 * @author Иванцов Дмитрий
 * @version 1.0
 */
@Schema(description = "Сущность статуса транзакции")
public record TransactionStatusDTO(@NotBlank(message = "Currency must not be blank")
                                   @Size(min = 5, max = 9)
                                   @Schema(description = "Статус транзакции в зависимости от решения сотрудника банка(ACCEPTED/REJECTED)", example = "REJECTED")
                                   String status) {
    /**
     * Метод для конвертации {@link TransactionStatus} в TransactionStatusDTO
     *
     * @param transactionStatus Статус транзакции в {@link String} формате
     * @return {@link TransactionStatusDTO}
     */
    public static TransactionStatusDTO map(TransactionStatus transactionStatus) {
        return new TransactionStatusDTO(transactionStatus.name());
    }
}
