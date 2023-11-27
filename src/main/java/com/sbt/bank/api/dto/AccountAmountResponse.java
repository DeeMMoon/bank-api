package com.sbt.bank.api.dto;

import com.sbt.bank.api.models.Account;
import com.sbt.bank.api.models.Currency;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Record ответа с информацией о {@link Account#amount балансе} счёта в стандартном представлении
 * т.е рубли, доллары и т.д и {@link Currency валюте} счёта
 *
 * @author Иванцов Дмитрий
 * @version 1.0
 * @see Account
 * @see Currency
 */
@Schema(description = "Ответ содержащий баланс банковского счёта и его валюта")
public record AccountAmountResponse(@Schema(description = "Баланс", example = "10000")
                                    BigDecimal amount,
                                    @Schema(description = "Валюта", example = "USD")
                                    Currency currency) {
    /**
     * Метод конвертации {@link Account счёта} в {@link AccountAmountResponse}
     *
     * @param account счёт, который нужно конвертировать
     * @return {@link AccountAmountResponse}
     */
    public static AccountAmountResponse map(Account account) {
        return new AccountAmountResponse(
                account.getAmount().divide(new BigDecimal(100)).setScale(2, RoundingMode.HALF_UP),
                account.getCurrency());
    }
}
