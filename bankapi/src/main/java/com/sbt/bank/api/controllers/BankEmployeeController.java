package com.sbt.bank.api.controllers;

import com.sbt.bank.api.dto.ClientDTO;
import com.sbt.bank.api.exceptions.client.ClientNotFoundException;
import com.sbt.bank.api.models.Client;
import com.sbt.bank.api.models.ClientInfo;
import com.sbt.bank.api.services.IClientService;
import jakarta.validation.Valid;
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

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/staff")
public class BankEmployeeController {

    private final IClientService clientService;

    public BankEmployeeController(IClientService clientService) {
        this.clientService = clientService;
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
    public ClientDTO getClientProfile(@PathVariable UUID clientId, @RequestBody @Valid ClientDTO clientDTO){
        var client = clientService.findClientById(clientId);
        if (client.isEmpty()){
            throw new ClientNotFoundException("Client with id: " + clientId + " not found");
        }
        return ClientDTO.map(client.get());
    }
//
//    @ResponseStatus(HttpStatus.CREATED)
//    @PostMapping("/clients/{clientId}")
//    public ClientDTO createAccount(@PathVariable String clientId, @RequestBody @Valid ClientDTO clientDTO){
//
//        return new ClientDTO();
//    }
//
//    @ResponseStatus(HttpStatus.OK)
//    @PatchMapping("/transactions")
//    public ClientDTO transactionApprove(@PathVariable String clientId, @RequestBody @Valid ClientDTO clientDTO){
//
//        return new ClientDTO();
//    }
}
