package com.sbt.bank.api.dto;

import com.sbt.bank.api.models.Account;
import java.util.UUID;

public record AccountResponseDTO(UUID id, String accountNumber) {
    public static AccountResponseDTO map(Account account) {
        return new AccountResponseDTO(account.getId(), account.getAccountNumber());
    }
}
