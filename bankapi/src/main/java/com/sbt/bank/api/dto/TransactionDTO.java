package com.sbt.bank.api.dto;

import com.sbt.bank.api.models.Account;
import com.sbt.bank.api.models.Transaction;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;

import java.math.BigDecimal;
public record TransactionDTO(@NotBlank(message = "Sender's account number must not be blank")
                             @Pattern(message = "Incorrect account number", regexp = "^[0-9]{20}$")
                             String accountNumberSender,
                             @NotBlank(message = "Recipient's account number must not be blank")
                             @Pattern(message = "Incorrect account number", regexp = "^[0-9]{20}$")
                             String accountNumberRecipient,
                             @NotNull(message = "Amount must not be null")
                             @DecimalMin(value = "0.00", inclusive = false, message = "Amount must be more 0.00")
                             @DecimalMax(value = "1000000.00", inclusive = false, message = "Amount must be less than 1 000 000.00")
                             @Digits(integer=6, fraction=2, message = "Incorrect amount input")
                             BigDecimal amount) {
    public static TransactionDTO map(Transaction transaction){
        return new TransactionDTO(
                transaction.getAccountNumberSender(),
                transaction.getAccountNumberRecipient(),
                transaction.getAmount().divide(new BigDecimal(100)));
    }
}
