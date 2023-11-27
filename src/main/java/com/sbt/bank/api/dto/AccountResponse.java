package com.sbt.bank.api.dto;

import com.sbt.bank.api.models.Account;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

/**
 * Record ответа, использующейся как результат работы endpoint, возвращающего <b>id</b> и <b>accountNumber</b>
 *
 * @author Иванцов Дмитрий
 * @version 1.0
 * @see Account
 */
@Schema(description = "Ответ содержащий ID банковского счёта и его номер")
public record AccountResponse(
        @Schema(description = "ID банковского счёта", example = "4f9a97c4-8300-11ee-b962-0242ac120001")
        UUID id,
        @Schema(description = "Номер банковского счета", example = "12345678910111213150")
        String accountNumber) {
    /**
     * Метод конвертации {@link Account счёта} в {@link AccountResponse}
     *
     * @param account счёт, который нужно конвертировать
     * @return {@link AccountResponse}
     */
    public static AccountResponse map(Account account) {
        return new AccountResponse(account.getId(), account.getAccountNumber());
    }
}
