package com.sbt.bank.api.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sbt.bank.api.dto.ClientDTO;
import com.sbt.bank.api.dto.CurrencyDTO;
import com.sbt.bank.api.dto.TransactionStatusDTO;
import com.sbt.bank.api.models.Client;
import com.sbt.bank.api.models.ClientInfo;
import com.sbt.bank.api.models.Currency;
import com.sbt.bank.api.models.Gender;
import com.sbt.bank.api.models.Transaction;
import com.sbt.bank.api.models.TransactionStatus;
import com.sbt.bank.api.repositories.TransactionRepository;
import com.sbt.bank.api.services.IClientService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@IT
class BankEmployeeControllerTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    private IClientService clientService;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private TransactionRepository transactionRepository;

    @Test
    void createClientProfile() throws Exception {
        ClientInfo clientInfo = ClientInfo.builder()
                .personalID("2323 123E42")
                .firstName("John")
                .lastName("Miles")
                .age(45)
                .email("test123@test.com")
                .gender(Gender.MAN)
                .phone("+7(909)8898888")
                .address("Moscow")
                .build();
        ClientDTO clientDTO = new ClientDTO(clientInfo);
        ResultActions response = mockMvc.perform(post("/api/v1/staff/clients")
                .content(objectMapper.writeValueAsString(clientDTO))
                .contentType(MediaType.APPLICATION_JSON));
        response.andExpect(status().isCreated());
        response.andExpect(jsonPath("$.clientInfo.personalID").value(clientInfo.getPersonalID()));
        response.andExpect(jsonPath("$.clientInfo.firstName").value(clientInfo.getFirstName()));
        response.andExpect(jsonPath("$.clientInfo.lastName").value(clientInfo.getLastName()));
        response.andExpect(jsonPath("$.clientInfo.age").value(clientInfo.getAge()));
        response.andExpect(jsonPath("$.clientInfo.email").value(clientInfo.getEmail()));
        response.andExpect(jsonPath("$.clientInfo.gender").value(Gender.MAN.toString()));
        response.andExpect(jsonPath("$.clientInfo.phone").value(clientInfo.getPhone()));
        response.andExpect(jsonPath("$.clientInfo.address").value(clientInfo.getAddress()));
    }

    @Test
    void createClientProfileBadRequest() throws Exception {
        ClientInfo clientInfo = ClientInfo.builder()
                .personalID("2")
                .firstName("John")
                .lastName("Miles")
                .age(45)
                .email("test123@test.com")
                .gender(Gender.MAN)
                .phone("+7(909)8898888")
                .address("Moscow")
                .build();
        ClientDTO clientDTO = new ClientDTO(clientInfo);
        ResultActions response = mockMvc.perform(post("/api/v1/staff/clients")
                .content(objectMapper.writeValueAsString(clientDTO))
                .contentType(MediaType.APPLICATION_JSON));
        response.andExpect(status().is4xxClientError());
    }

    @Test
    void updateClientProfile() throws Exception {
        Client client = clientService.findClientById(UUID.fromString("4f9a97c4-8300-11ee-b962-0242ac120010"));
        ClientInfo clientInfo = ClientInfo.builder()
                .personalID("2323 123E42")
                .firstName("John")
                .lastName("Miles")
                .age(45)
                .email("test123@test.com")
                .gender(Gender.MAN)
                .phone("+7(909)8898888")
                .address("Moscow")
                .build();
        ClientDTO clientDTO = new ClientDTO(clientInfo);
        ResultActions response = mockMvc.perform(put("/api/v1/staff/clients/" + client.getId())
                .content(objectMapper.writeValueAsString(clientDTO))
                .contentType(MediaType.APPLICATION_JSON));
        response.andExpect(status().isOk());
        response.andExpect(jsonPath("$.clientInfo.personalID").value(clientInfo.getPersonalID()));
        response.andExpect(jsonPath("$.clientInfo.firstName").value(clientInfo.getFirstName()));
        response.andExpect(jsonPath("$.clientInfo.lastName").value(clientInfo.getLastName()));
        response.andExpect(jsonPath("$.clientInfo.age").value(clientInfo.getAge()));
        response.andExpect(jsonPath("$.clientInfo.email").value(clientInfo.getEmail()));
        response.andExpect(jsonPath("$.clientInfo.gender").value(Gender.MAN.toString()));
        response.andExpect(jsonPath("$.clientInfo.phone").value(clientInfo.getPhone()));
        response.andExpect(jsonPath("$.clientInfo.address").value(clientInfo.getAddress()));
    }

    @Test
    void updateClientProfileBadRequest() throws Exception {
        Client client = clientService.findClientById(UUID.fromString("4f9a97c4-8300-11ee-b962-0242ac120010"));
        ClientInfo clientInfo = ClientInfo.builder()
                .personalID("2323 123E42")
                .firstName("John")
                .lastName("Miles")
                .age(45)
                .email("test123@test.com")
                .gender(Gender.MAN)
                .phone("+7(909)888")
                .address("Moscow")
                .build();
        ClientDTO clientDTO = new ClientDTO(clientInfo);
        ResultActions response = mockMvc.perform(put("/api/v1/staff/clients/" + client.getId())
                .content(objectMapper.writeValueAsString(clientDTO))
                .contentType(MediaType.APPLICATION_JSON));
        response.andExpect(status().is4xxClientError());
    }

    @Test
    void updateClientProfileBadRequestSamePersonalId() throws Exception {
        Client client = clientService.findClientById(UUID.fromString("4f9a97c4-8300-11ee-b962-0242ac120010"));
        ClientInfo clientInfo = ClientInfo.builder()
                .personalID("2323 123E42")
                .firstName("John")
                .lastName("Miles")
                .age(45)
                .email("test123@test.com")
                .gender(Gender.MAN)
                .phone("+7(909)8888888")
                .address("Moscow")
                .build();
        ClientDTO clientDTO = new ClientDTO(clientInfo);
        ResultActions response = mockMvc.perform(put("/api/v1/staff/clients/" + client.getId())
                .content(objectMapper.writeValueAsString(clientDTO))
                .contentType(MediaType.APPLICATION_JSON));
        response.andExpect(status().isOk());
        client = clientService.findClientById(UUID.fromString("4f9a97c4-8300-11ee-b962-0242ac120011"));
        clientInfo = ClientInfo.builder()
                .personalID("2323 123E42")
                .firstName("John")
                .lastName("Miles")
                .age(45)
                .email("test123@test.com")
                .gender(Gender.MAN)
                .phone("+7(909)8888888")
                .address("Moscow")
                .build();
        clientDTO = new ClientDTO(clientInfo);
        response = mockMvc.perform(put("/api/v1/staff/clients/" + client.getId())
                .content(objectMapper.writeValueAsString(clientDTO))
                .contentType(MediaType.APPLICATION_JSON));
        response.andExpect(status().is4xxClientError());
    }

    @Test
    void getClientProfile() throws Exception {
        var client = clientService.findClientById(UUID.fromString("4f9a97c4-8300-11ee-b962-0242ac120010"));
        var clientInfo = client.getClientInfo();
        ResultActions response = mockMvc.perform(get("/api/v1/staff/clients/" + client.getId()));
        response.andExpect(status().isOk());
        response.andExpect(jsonPath("$.clientInfo.personalID").value(clientInfo.getPersonalID()));
        response.andExpect(jsonPath("$.clientInfo.firstName").value(clientInfo.getFirstName()));
        response.andExpect(jsonPath("$.clientInfo.lastName").value(clientInfo.getLastName()));
        response.andExpect(jsonPath("$.clientInfo.age").value(clientInfo.getAge()));
        response.andExpect(jsonPath("$.clientInfo.email").value(clientInfo.getEmail()));
        response.andExpect(jsonPath("$.clientInfo.gender").value(Gender.MAN.toString()));
        response.andExpect(jsonPath("$.clientInfo.phone").value(clientInfo.getPhone()));
        response.andExpect(jsonPath("$.clientInfo.address").value(clientInfo.getAddress()));
    }

    @Test
    void getClientProfileNotFound() throws Exception {
        ResultActions response = mockMvc.perform(get("/api/v1/staff/clients/" + "4f9a97c4-8300-11ee-b962-0242ac120099"));
        response.andExpect(status().isNotFound());
    }

    @Test
    void createAccount() throws Exception {
        CurrencyDTO currencyDTO = new CurrencyDTO("RUR");
        var client = clientService.findClientById(UUID.fromString("4f9a97c4-8300-11ee-b962-0242ac120010"));
        ResultActions response = mockMvc.perform(post("/api/v1/staff/clients/" + client.getId())
                .content(objectMapper.writeValueAsString(currencyDTO))
                .contentType(MediaType.APPLICATION_JSON));
        response.andExpect(status().isCreated());
        response.andExpect(jsonPath("$.id").isNotEmpty());
        response.andExpect(jsonPath("$.accountNumber").isString());
    }

    @Test
    void createAccountBadRequest() throws Exception {
        CurrencyDTO currencyDTO = new CurrencyDTO("USK");
        var client = clientService.findClientById(UUID.fromString("4f9a97c4-8300-11ee-b962-0242ac120010"));
        ResultActions response = mockMvc.perform(post("/api/v1/staff/clients/" + client.getId())
                .content(objectMapper.writeValueAsString(currencyDTO))
                .contentType(MediaType.APPLICATION_JSON));
        response.andExpect(status().is4xxClientError());
    }

    @Test
    void transactionApprove() throws Exception {
        var transaction = Transaction.builder()
                .id(UUID.randomUUID())
                .accountNumberSender("12345678910111213150")
                .accountNumberRecipient("12345678910111213148")
                .currencyTypeSender(Currency.RUR)
                .currencyTypeRecipient(Currency.RUR)
                .amount(BigDecimal.valueOf(100))
                .sendingTime(LocalDateTime.now())
                .updatedStatusTime(LocalDateTime.now())
                .transactionStatus(TransactionStatus.PROCESSED)
                .build();
        transactionRepository.save(transaction);
        var transactionStatusDTO = new TransactionStatusDTO("ACCEPTED");
        ResultActions response = mockMvc.perform(patch("/api/v1/staff/transactions/expectation/" + transaction.getId())
                .content(objectMapper.writeValueAsString(transactionStatusDTO))
                .contentType(MediaType.APPLICATION_JSON));
        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("ACCEPTED"));
    }

    @Test
    void transactionApproveReject() throws Exception {
        var transaction = Transaction.builder()
                .id(UUID.randomUUID())
                .accountNumberSender("12345678910111213150")
                .accountNumberRecipient("12345678910111213148")
                .currencyTypeSender(Currency.RUR)
                .currencyTypeRecipient(Currency.RUR)
                .amount(BigDecimal.valueOf(100))
                .sendingTime(LocalDateTime.now())
                .updatedStatusTime(LocalDateTime.now())
                .transactionStatus(TransactionStatus.PROCESSED)
                .build();
        transactionRepository.save(transaction);
        var transactionStatusDTO = new TransactionStatusDTO("REJECTED");
        ResultActions response = mockMvc.perform(patch("/api/v1/staff/transactions/expectation/" + transaction.getId())
                .content(objectMapper.writeValueAsString(transactionStatusDTO))
                .contentType(MediaType.APPLICATION_JSON));
        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("REJECTED"));
    }

    @Test
    void transactionApproveBadRequest() throws Exception {
        var transaction = Transaction.builder()
                .id(UUID.randomUUID())
                .accountNumberSender("12345678910111213150")
                .accountNumberRecipient("12345678910111213148")
                .currencyTypeSender(Currency.RUR)
                .currencyTypeRecipient(Currency.RUR)
                .amount(BigDecimal.valueOf(100))
                .sendingTime(LocalDateTime.now())
                .updatedStatusTime(LocalDateTime.now())
                .transactionStatus(TransactionStatus.PROCESSED)
                .build();
        transactionRepository.save(transaction);
        var transactionStatusDTO = new TransactionStatusDTO("ACCEP");
        ResultActions response = mockMvc.perform(patch("/api/v1/staff/transactions/expectation/" + transaction.getId())
                .content(objectMapper.writeValueAsString(transactionStatusDTO))
                .contentType(MediaType.APPLICATION_JSON));
        response.andExpect(status().is4xxClientError());
    }

    @Test
    void transactionApproveFAIL() throws Exception {
        var transaction = Transaction.builder()
                .id(UUID.randomUUID())
                .accountNumberSender("12345678910111213150")
                .accountNumberRecipient("12345678910111213148")
                .currencyTypeSender(Currency.RUR)
                .currencyTypeRecipient(Currency.RUR)
                .amount(BigDecimal.valueOf(500))
                .sendingTime(LocalDateTime.now())
                .updatedStatusTime(LocalDateTime.now())
                .transactionStatus(TransactionStatus.PROCESSED)
                .build();
        transactionRepository.save(transaction);
        var transaction1 = Transaction.builder()
                .id(UUID.randomUUID())
                .accountNumberSender("12345678910111213150")
                .accountNumberRecipient("12345678910111213148")
                .currencyTypeSender(Currency.RUR)
                .currencyTypeRecipient(Currency.RUR)
                .amount(BigDecimal.valueOf(700))
                .sendingTime(LocalDateTime.now())
                .updatedStatusTime(LocalDateTime.now())
                .transactionStatus(TransactionStatus.PROCESSED)
                .build();
        transactionRepository.save(transaction1);
        var transactionStatusDTO = new TransactionStatusDTO("ACCEPTED");
        ResultActions response = mockMvc.perform(patch("/api/v1/staff/transactions/expectation/" + transaction1.getId())
                .content(objectMapper.writeValueAsString(transactionStatusDTO))
                .contentType(MediaType.APPLICATION_JSON));
        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("ACCEPTED"));
        response = mockMvc.perform(patch("/api/v1/staff/transactions/expectation/" + transaction.getId())
                .content(objectMapper.writeValueAsString(transactionStatusDTO))
                .contentType(MediaType.APPLICATION_JSON));
        response.andExpect(status().is4xxClientError());
    }
}