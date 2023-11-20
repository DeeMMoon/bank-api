package com.sbt.bank.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record TransactionStatusRequest(@NotBlank(message = "Currency must not be blank") @Size(min = 5, max = 9) String status){
}
