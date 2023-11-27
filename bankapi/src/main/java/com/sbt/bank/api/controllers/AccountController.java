package com.sbt.bank.api.controllers;

import com.sbt.bank.api.dto.AccountAmountResponse;
import com.sbt.bank.api.dto.AccountRequest;
import com.sbt.bank.api.dto.AccountResponse;
import com.sbt.bank.api.services.IAccountService;
import com.sbt.bank.api.services.IClientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@Validated
@RequestMapping("api/v1/{clientId}")
@Tag(name = "Банковский счет", description = "Управление банковскими счетами клиентов")
public class AccountController {

    private final IAccountService accountService;
    private final IClientService clientService;

    public AccountController(IAccountService accountService, IClientService clientService) {
        this.accountService = accountService;
        this.clientService = clientService;
    }

    @Operation(
            summary = "Получение банковских счетов клиента",
            description = "Позволяет получить список банковских счетов клиента в виде id и номер счёта"
    )
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/accounts")
    public List<AccountResponse> getClientAccounts(@PathVariable(value = "clientId") @Parameter(description = "Идентификатор клиента") UUID id) {
        var accounts = clientService.getAccountsByClientId(id);
        return new ArrayList<AccountResponse>(accounts.stream()
                .map(AccountResponse::map)
                .collect(Collectors.toList())) {
        };
    }

    @Operation(
            summary = "Добавление средств на счёт клиента",
            description = "Позволяет добавить на баланс переданную сумму в формате разменной денежной единице"
    )
    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("/accounts")
    public AccountResponse addDepositToTheAccountBalance(@PathVariable(value = "clientId") @Parameter(description = "Идентификатор клиента") UUID id,
                                                         @RequestBody @Valid AccountRequest accountRequest) {
        var accounts = clientService.getAccountsByClientId(id);
        var account = accountService.findAccountByAccountNumber(accountRequest.accountNumber(), accounts);
        return AccountResponse.map(accountService.addDepositToTheAccountBalance(Optional.ofNullable(account), accountRequest.amount()));
    }

    @Operation(
            summary = "Получение баланса счёта",
            description = "Позволяет получить баланс счёта и его валюту"
    )
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/accounts/{accountNumber}")
    public AccountAmountResponse getAccountAmount(@PathVariable(value = "clientId") @Parameter(description = "Идентификатор клиента") UUID id,
                                                  @PathVariable(value = "accountNumber")
                                                  @Pattern(message = "Incorrect account number", regexp = "^[0-9]{20}$")
                                                  @Parameter(description = "Номер банковского счёта") String accountNumber) {
        var account = accountService.findAccountByAccountNumber(accountNumber, clientService.getAccountsByClientId(id));
        return AccountAmountResponse.map(account);
    }
}
