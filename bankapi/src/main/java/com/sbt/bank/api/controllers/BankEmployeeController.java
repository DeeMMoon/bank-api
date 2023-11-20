package com.sbt.bank.api.controllers;

import com.sbt.bank.api.dto.AccountResponseDTO;
import com.sbt.bank.api.dto.ClientDTO;
import com.sbt.bank.api.dto.CurrencyDTO;
import com.sbt.bank.api.dto.TransactionStatusRequest;
import com.sbt.bank.api.exceptions.currency.CurrencyException;
import com.sbt.bank.api.exceptions.currency.CurrencyNotFoundException;
import com.sbt.bank.api.exceptions.transaction.IllegalTransactionException;
import com.sbt.bank.api.exceptions.transaction.TransactionNotFoundException;
import com.sbt.bank.api.exceptions.transaction.TransactionStatusNotFoundException;
import com.sbt.bank.api.models.Currency;
import com.sbt.bank.api.models.TransactionStatus;
import com.sbt.bank.api.services.IAccountService;
import com.sbt.bank.api.services.IClientService;
import com.sbt.bank.api.services.ITransactionService;
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
public class BankEmployeeController {

    private final IClientService clientService;
    private final IAccountService accountService;
    private final ITransactionService transactionService;

    public BankEmployeeController(IClientService clientService, IAccountService accountService, ITransactionService transactionService) {
        this.clientService = clientService;
        this.accountService = accountService;
        this.transactionService = transactionService;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/clients")
    public ClientDTO createClientProfile(@RequestBody @Valid ClientDTO clientDTO){
        var client = clientService.createClient(clientDTO);
        return ClientDTO.map(client);
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/clients/{clientId}")
    public ClientDTO updateClientProfile(@PathVariable UUID clientId, @RequestBody @Valid ClientDTO clientDTO){
        var client = clientService.updateClientPersonalInformation(clientId, clientDTO);
        return  ClientDTO.map(client);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/clients/{clientId}")
    public ClientDTO getClientProfile(@PathVariable UUID clientId){
        var client = clientService.findClientById(clientId);
        return ClientDTO.map(client);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/clients/{clientId}")
    public AccountResponseDTO createAccount(@PathVariable UUID clientId, @RequestBody @Valid CurrencyDTO currencyDTO){
        if (!EnumUtils.isValidEnum(Currency.class, currencyDTO.currency().toUpperCase())) {
            throw new CurrencyNotFoundException("Currency with value: " + currencyDTO.currency() + " not found");
        }
        var currency = Currency.valueOf(currencyDTO.currency().toUpperCase());
        var client = clientService.findClientById(clientId);
        var account = accountService.createAccount(client, currency);
        return AccountResponseDTO.map(account);
    }

    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("/transactions/expectation/{transactionId}")
    public void transactionApprove(@PathVariable UUID transactionId, @RequestBody @Valid TransactionStatusRequest statusRequest){
        if (!EnumUtils.isValidEnum(TransactionStatus.class, statusRequest.status().toUpperCase())) {
            throw new TransactionStatusNotFoundException("Transaction status with value: " + statusRequest.status() + " not found");
        }
        // TODO Вынести блок с проверками в отдельный метод сервиса
        var transaction = transactionService.findTransactionById(transactionId);
        if (transaction.isEmpty()){
            throw new TransactionNotFoundException("Transaction with id: " + transactionId + " not found");
        }
        if (!transaction.get().getTransactionStatus().equals(TransactionStatus.PROCESSED)){
            throw new IllegalTransactionException("The transaction status cannot be changed because it was changed earlier");
        }
        var status = TransactionStatus.valueOf(statusRequest.status().toUpperCase());
        if (status.equals(TransactionStatus.REJECTED)){
            transactionService.updateTransactionStatus(TransactionStatus.REJECTED, transaction.get());
        }
        if (status.equals(TransactionStatus.ACCEPTED)){
            //TODO Добавить проверку после doTrans, if status != ACCEPTED -> update FAIL и проверить
            transactionService.updateTransactionStatus(TransactionStatus.FAIL, transaction.get());
            transactionService.doTransaction(transaction.get());
        }
        // TODO Добавить возвращение статуса Транзакции
    }
}
