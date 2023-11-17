package com.sbt.bank.api.controllers;

import com.sbt.bank.api.dto.ClientDTO;
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

@RestController
@RequestMapping("/api/v1/staff")
public class BankEmployeeController {

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/clients")
    public ClientDTO createClientProfile(@RequestBody @Valid ClientDTO clientDTO){

        return new ClientDTO();
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/clients/{clientId}")
    public ClientDTO updateClientProfile(@PathVariable String clientId, @RequestBody @Valid ClientDTO clientDTO){

        return new ClientDTO();
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/clients/{clientId}")
    public ClientDTO getClientProfile(@PathVariable String clientId, @RequestBody @Valid ClientDTO clientDTO){

        return new ClientDTO();
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/clients/{clientId}")
    public ClientDTO createAccount(@PathVariable String clientId, @RequestBody @Valid ClientDTO clientDTO){

        return new ClientDTO();
    }

    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("/transactions")
    public ClientDTO transactionApprove(@PathVariable String clientId, @RequestBody @Valid ClientDTO clientDTO){

        return new ClientDTO();
    }
}
