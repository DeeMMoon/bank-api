package com.sbt.bank.api.controllers;

import com.sbt.bank.api.dto.AccountResponse;
import com.sbt.bank.api.dto.ClientDTO;
import com.sbt.bank.api.dto.CurrencyDTO;
import com.sbt.bank.api.dto.TransactionStatusDTO;
import com.sbt.bank.api.exceptions.currency.CurrencyNotFoundException;
import com.sbt.bank.api.models.Currency;
import com.sbt.bank.api.models.TransactionStatus;
import com.sbt.bank.api.services.IAccountService;
import com.sbt.bank.api.services.IClientService;
import com.sbt.bank.api.services.ITransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.apache.commons.lang3.EnumUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/staff")
@Tag(name = "Сотрудник банка", description = "Взаимодействие с клиентами и их счетами")
public class BankEmployeeController {

    private final IClientService clientService;
    private final IAccountService accountService;
    private final ITransactionService transactionService;

    public BankEmployeeController(IClientService clientService, IAccountService accountService, ITransactionService transactionService) {
        this.clientService = clientService;
        this.accountService = accountService;
        this.transactionService = transactionService;
    }

    @Operation(
            summary = "Создание нового клиента",
            description = "Позволяет создать профиль нового клиента на основе его личной информации"
    )
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/clients")
    public ClientDTO createClientProfile(@RequestBody @Valid ClientDTO clientDTO) {
        var client = clientService.createClient(clientDTO);
        return ClientDTO.map(client);
    }

    @Operation(
            summary = "Обновление информации о клиенте",
            description = "Позволяет обновить личную информацию о клиента на основе переданных данных"
    )
    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/clients/{clientId}")
    public ClientDTO updateClientProfile(@PathVariable @Parameter(description = "Идентификатор пользователя")
                                                 UUID clientId, @RequestBody @Valid ClientDTO clientDTO) {
        var client = clientService.updateClientPersonalInformation(clientId, clientDTO);
        return ClientDTO.map(client);
    }

    @Operation(
            summary = "Получение информации о клиенте",
            description = "Позволяет получить личную информацию о клиента"
    )
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/clients/{clientId}")
    public ClientDTO getClientProfile(@PathVariable @Parameter(description = "Идентификатор клиента") UUID clientId) {
        var client = clientService.findClientById(clientId);
        return ClientDTO.map(client);
    }

    @Operation(
            summary = "Открытие нового банковского счету у клиенту",
            description = "Позволяет создать новый банковский счет, на основе переданной валюты"
    )
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/clients/{clientId}")
    public AccountResponse createAccount(@PathVariable @Parameter(description = "Идентификатор клиента") UUID clientId,
                                         @RequestBody @Valid CurrencyDTO currencyDTO) {
        if (!EnumUtils.isValidEnum(Currency.class, currencyDTO.currency().toUpperCase())) {
            throw new CurrencyNotFoundException("Currency with value: " + currencyDTO.currency() + " not found");
        }
        var currency = Currency.valueOf(currencyDTO.currency().toUpperCase());
        var client = clientService.findClientById(clientId);
        var account = accountService.createAccount(client, currency);
        return AccountResponse.map(account);
    }

    @Operation(
            summary = "Подтверждение/Отказ в переводе средств",
            description = "Позволяет подтвердить или отклонить перевод денежных средств между пользователями"
    )
    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("/transactions/expectation/{transactionId}")
    public TransactionStatusDTO transactionApprove(@PathVariable UUID transactionId, @RequestBody @Valid TransactionStatusDTO transactionStatusDTO) {
        var transaction = transactionService.approveTransactionValidation(transactionStatusDTO, transactionId);
        var status = TransactionStatus.valueOf(transactionStatusDTO.status().toUpperCase());
        if (status.equals(TransactionStatus.REJECTED)) {
            transactionService.updateTransactionStatus(TransactionStatus.REJECTED, transaction);
        }
        if (status.equals(TransactionStatus.ACCEPTED)) {
            transactionService.doTransaction(transaction);
            if (transaction.getTransactionStatus().compareTo(TransactionStatus.ACCEPTED) != 0) {
                transactionService.updateTransactionStatus(TransactionStatus.FAIL, transaction);
            }
        }
        return TransactionStatusDTO.map(transaction.getTransactionStatus());
    }
}
