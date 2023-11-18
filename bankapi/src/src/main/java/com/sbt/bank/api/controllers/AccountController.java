package com.sbt.bank.api.controllers;

import com.sbt.bank.api.dto.AccountAmountResponse;
import com.sbt.bank.api.dto.AccountRequestDTO;
import com.sbt.bank.api.dto.AccountResponseDTO;
import com.sbt.bank.api.models.Account;
import com.sbt.bank.api.services.IAccountService;
import com.sbt.bank.api.services.IClientService;
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
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@Validated
@RequestMapping("api/v1")
public class AccountController {

    private final IAccountService accountService;
    private final IClientService clientService;

    public AccountController(IAccountService accountService, IClientService clientService) {
        this.accountService = accountService;
        this.clientService = clientService;
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{clientId}/accounts")
    public List<AccountResponseDTO> getClientAccounts(@PathVariable(value = "clientId") UUID id) {
        var accounts = clientService.getAccountsByClientId(id);
        return new ArrayList<AccountResponseDTO>(accounts.stream()
                .map(AccountResponseDTO::map)
                .collect(Collectors.toList())) {
        };
    }

    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("/{clientId}/accounts")
    public AccountResponseDTO addDepositToTheAccountBalance(@PathVariable(value = "clientId") UUID id,
                                              @RequestBody @Valid AccountRequestDTO accountRequestDTO) {
        var accounts = clientService.getAccountsByClientId(id);
        Account account = accountService.findAccountByAccountNumber(accountRequestDTO.accountNumber(), accounts);
        return AccountResponseDTO.map(accountService.addDepositToTheAccountBalance(account, accountRequestDTO.amount()));
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{clientId}/accounts/{accountNumber}")
    public AccountAmountResponse getAccountAmount(@PathVariable(value = "clientId") UUID id,
                                                  @PathVariable(value = "accountNumber")
                                                  @Pattern(message = "Incorrect account number", regexp = "^[0-9]{20}$") String accountNumber) {
        var account = accountService.findAccountByAccountNumber(accountNumber, clientService.getAccountsByClientId(id));
        return AccountAmountResponse.map(account);
    }
}
