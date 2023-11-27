package com.sbt.bank.api.dto;

import com.sbt.bank.api.models.Transaction;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.math.BigDecimal;

/**
 * Record DTO транзакции с полями <b>accountNumberSender</b>, <b>accountNumberRecipient</b> и <b>amount</b>
 *
 * @author Иванцов Дмитрий
 * @version 1.0
 * @see Transaction
 */
@Schema(description = "Сущность транзакции")
public record TransactionDTO(@NotBlank(message = "Sender's account number must not be blank")
                             @Pattern(message = "Incorrect account number", regexp = "^[0-9]{20}$")
                             @Schema(description = "Номер банковского счета отправителя", example = "12345678910111213150")
                             String accountNumberSender,
                             @NotBlank(message = "Recipient's account number must not be blank")
                             @Pattern(message = "Incorrect account number", regexp = "^[0-9]{20}$")
                             @Schema(description = "Номер банковского счета получателя", example = "12345678910111213148")
                             String accountNumberRecipient,
                             @NotNull(message = "Amount must not be null")
                             @DecimalMin(value = "0.00", inclusive = false, message = "Amount must be more 0.00")
                             @DecimalMax(value = "1000000.00", inclusive = false, message = "Amount must be less than 1 000 000.00")
                             @Digits(integer = 6, fraction = 2, message = "Incorrect amount input")
                             @Schema(description = "Сумма перевода", example = "50")
                             BigDecimal amount) {
    /**
     * Метод конвертации {@link Transaction транзакции} в {@link TransactionDTO}
     *
     * @param transaction Транзакция, которую нужно конвертировать
     * @return {@link TransactionDTO}
     */
    public static TransactionDTO map(Transaction transaction) {
        return new TransactionDTO(
                transaction.getAccountNumberSender(),
                transaction.getAccountNumberRecipient(),
                transaction.getAmount().divide(new BigDecimal(100)));
    }
}
