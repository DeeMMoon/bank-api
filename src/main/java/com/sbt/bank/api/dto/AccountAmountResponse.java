package com.sbt.bank.api.dto;

import com.sbt.bank.api.models.Account;
import com.sbt.bank.api.models.Currency;
import java.math.BigDecimal;
import java.math.RoundingMode;

public record AccountAmountResponse(BigDecimal amount, Currency currency) {
    public static AccountAmountResponse map(Account account){
        return new AccountAmountResponse(
                account.getAmount().divide(new BigDecimal(100)).setScale(2, RoundingMode.HALF_UP),
                account.getCurrency());
    }
}
