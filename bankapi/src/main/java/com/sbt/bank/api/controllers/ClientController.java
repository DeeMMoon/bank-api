package com.sbt.bank.api.controllers;

import com.sbt.bank.api.dto.TransactionDTO;
import com.sbt.bank.api.exceptions.account.AccountIsBlockedException;
import com.sbt.bank.api.models.Transaction;
import com.sbt.bank.api.models.TransactionStatus;
import com.sbt.bank.api.services.IAccountService;
import com.sbt.bank.api.services.IClientService;
import com.sbt.bank.api.services.ITransactionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/clients")
public class ClientController {

    private final IClientService clientService;
    private final ITransactionService transactionService;
    private final IAccountService accountService;


    public ClientController(IClientService clientService, ITransactionService transactionService, IAccountService accountService) {
        this.clientService = clientService;
        this.transactionService = transactionService;
        this.accountService = accountService;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/{clientId}/transfer")
    public TransactionDTO sendAmountToClient(@PathVariable(value = "clientId") UUID id,
                                   @RequestBody @Valid TransactionDTO transactionDTO) {
        accountService.findAccountByAccountNumber(transactionDTO.accountNumberSender(), clientService.getAccountsByClientId(id));
        var transaction = transactionService.createTransaction(transactionDTO);
        transaction = transactionService.updateTransactionStatus(TransactionStatus.PROCESSED, transaction).get();
        return TransactionDTO.map(transaction);
    }

}
