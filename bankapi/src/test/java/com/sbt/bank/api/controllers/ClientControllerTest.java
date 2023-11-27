package com.sbt.bank.api.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sbt.bank.api.dto.TransactionDTO;
import com.sbt.bank.api.services.IAccountService;
import com.sbt.bank.api.services.IClientService;
import com.sbt.bank.api.services.ITransactionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.math.BigDecimal;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@IT
class ClientControllerTest {

    @Autowired
    private IClientService clientService;
    @Autowired
    private ITransactionService transactionService;
    @Autowired
    private IAccountService accountService;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    MockMvc mockMvc;

    @Test
    void sendAmountToClient() throws Exception {
        var client = clientService.findClientById(UUID.fromString("4f9a97c4-8300-11ee-b962-0242ac120010"));
        var accountSender = accountService.findAccountByAccountNumber("12345678910111213150").get();
        var accountRecipient = accountService.findAccountByAccountNumber("12345678910111213148").get();

        TransactionDTO transactionDTO = new TransactionDTO(accountSender.getAccountNumber(), accountRecipient.getAccountNumber(), BigDecimal.valueOf(5));
        ResultActions response = mockMvc.perform(post("/api/v1/clients/" + client.getId() + "/transfer")
                .content(objectMapper.writeValueAsString(transactionDTO))
                .contentType(MediaType.APPLICATION_JSON));
        response.andExpect(status().isCreated())
                .andExpect(jsonPath("$.accountNumberSender").value("12345678910111213150"))
                .andExpect(jsonPath("$.accountNumberRecipient").value("12345678910111213148"))
                .andExpect(jsonPath("$.amount").value("5"));
    }

    @Test
    void sendAmountToClientNotFoundSender() throws Exception {
        var client = clientService.findClientById(UUID.fromString("4f9a97c4-8300-11ee-b962-0242ac120010"));
        var accountRecipient = accountService.findAccountByAccountNumber("12345678910111213148").get();

        TransactionDTO transactionDTO = new TransactionDTO("12345678910111213151", accountRecipient.getAccountNumber(), BigDecimal.valueOf(5));
        ResultActions response = mockMvc.perform(post("/api/v1/clients/" + client.getId() + "/transfer")
                .content(objectMapper.writeValueAsString(transactionDTO))
                .contentType(MediaType.APPLICATION_JSON));
        response.andExpect(status().isNotFound());
    }

    @Test
    void sendAmountToClientNotFoundRecipient() throws Exception {
        var client = clientService.findClientById(UUID.fromString("4f9a97c4-8300-11ee-b962-0242ac120010"));
        var accountSender = accountService.findAccountByAccountNumber("12345678910111213150").get();

        TransactionDTO transactionDTO = new TransactionDTO(accountSender.getAccountNumber(), "12345678910111213199", BigDecimal.valueOf(5));
        ResultActions response = mockMvc.perform(post("/api/v1/clients/" + client.getId() + "/transfer")
                .content(objectMapper.writeValueAsString(transactionDTO))
                .contentType(MediaType.APPLICATION_JSON));
        response.andExpect(status().isNotFound());
    }

    @Test
    void sendAmountToClientBadRequest() throws Exception {
        var client = clientService.findClientById(UUID.fromString("4f9a97c4-8300-11ee-b962-0242ac120010"));
        var accountSender = accountService.findAccountByAccountNumber("12345678910111213150").get();
        var accountRecipient = accountService.findAccountByAccountNumber("12345678910111213148").get();

        TransactionDTO transactionDTO = new TransactionDTO(accountSender.getAccountNumber(), accountRecipient.getAccountNumber(), BigDecimal.valueOf(0));
        ResultActions response = mockMvc.perform(post("/api/v1/clients/" + client.getId() + "/transfer")
                .content(objectMapper.writeValueAsString(transactionDTO))
                .contentType(MediaType.APPLICATION_JSON));
        response.andExpect(status().is4xxClientError());
    }
}