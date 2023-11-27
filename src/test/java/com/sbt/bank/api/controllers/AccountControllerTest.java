package com.sbt.bank.api.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sbt.bank.api.dto.AccountRequest;
import com.sbt.bank.api.services.IAccountService;
import com.sbt.bank.api.services.IClientService;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.math.BigDecimal;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@IT
class AccountControllerTest {

    @Autowired
    private IAccountService accountService;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private IClientService clientService;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private AccountController accountController;

    @Test
    void getClientAccounts() throws Exception {
        var client = clientService.findClientById(UUID.fromString("4f9a97c4-8300-11ee-b962-0242ac120010"));
        ResultActions response = mockMvc.perform(get("/api/v1/" + client.getId() + "/accounts"));
        response.andExpect(status().isOk());
        response.andExpect(jsonPath("$.size()", CoreMatchers.is(client.getAccounts().size())));

        var client1 = clientService.findClientById(UUID.fromString("4f9a97c4-8300-11ee-b962-0242ac120011"));
        response = mockMvc.perform(get("/api/v1/" + client1.getId() + "/accounts"));
        response.andExpect(status().isOk());
        response.andExpect(jsonPath("$.size()", CoreMatchers.is(client1.getAccounts().size())));

        var client2 = clientService.findClientById(UUID.fromString("4f9a97c4-8300-11ee-b962-0242ac120012"));
        response = mockMvc.perform(get("/api/v1/" + client2.getId() + "/accounts"));
        response.andExpect(status().isOk());
        response.andExpect(jsonPath("$.size()", CoreMatchers.is(client2.getAccounts().size())));
    }

    @Test
    void getClientAccountsNotFound() throws Exception {
        ResultActions response = mockMvc.perform(get("/api/v1/" + UUID.fromString("4f9a97c4-8300-11ee-b962-0242ac120099") + "/accounts"));
        response.andExpect(status().isNotFound());
    }

    @Test
    void addDepositToTheAccountBalance() throws Exception {
        var client = clientService.findClientById(UUID.fromString("4f9a97c4-8300-11ee-b962-0242ac120010"));
        AccountRequest request = new AccountRequest("12345678910111213150", BigDecimal.valueOf(100));
        ResultActions response = mockMvc.perform(patch("/api/v1/" + client.getId() + "/accounts")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON));
        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("4f9a97c4-8300-11ee-b962-0242ac120001"))
                .andExpect(jsonPath("$.accountNumber").value("12345678910111213150"));
    }

    @Test
    void addDepositToTheAccountBalanceNotFound() throws Exception {
        var client = clientService.findClientById(UUID.fromString("4f9a97c4-8300-11ee-b962-0242ac120010"));
        AccountRequest request = new AccountRequest("12345678910111213199", BigDecimal.valueOf(100));
        ResultActions response = mockMvc.perform(patch("/api/v1/" + client.getId() + "/accounts")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON));
        response.andExpect(status().isNotFound());
    }

    @Test
    void addDepositToTheAccountBalanceBadRequest() throws Exception {
        var client = clientService.findClientById(UUID.fromString("4f9a97c4-8300-11ee-b962-0242ac120010"));
        AccountRequest request = new AccountRequest("12345678910111213199", BigDecimal.valueOf(-13));
        ResultActions response = mockMvc.perform(patch("/api/v1/" + client.getId() + "/accounts")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON));
        response.andExpect(status().is4xxClientError());
    }

    @Test
    void getAccountAmount() throws Exception {
        var client = clientService.findClientById(UUID.fromString("4f9a97c4-8300-11ee-b962-0242ac120010"));
        var account = accountService.findAccountByAccountNumber("12345678910111213149").get();
        ResultActions response = mockMvc.perform(get("/api/v1/" + client.getId() + "/accounts/" + account.getAccountNumber()));
        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.amount").isNumber())
                .andExpect(jsonPath("$.amount").isNotEmpty())
                .andExpect(jsonPath("$.amount").value("1.0"))
                .andExpect(jsonPath("$.currency").isNotEmpty())
                .andExpect(jsonPath("$.currency").value("USD"))
        ;
    }

    @Test
    void getAccountAmountNotFound() throws Exception {
        var client = clientService.findClientById(UUID.fromString("4f9a97c4-8300-11ee-b962-0242ac120010"));
        var account = accountService.findAccountByAccountNumber("12345678910111213148").get();
        ResultActions response = mockMvc.perform(get("/api/v1/" + client.getId() + "/accounts/" + account.getAccountNumber()));
        response.andExpect(status().isNotFound());
    }
}