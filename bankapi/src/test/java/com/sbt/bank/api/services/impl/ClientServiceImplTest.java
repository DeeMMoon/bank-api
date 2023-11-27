package com.sbt.bank.api.services.impl;

import com.sbt.bank.api.dto.ClientDTO;
import com.sbt.bank.api.exceptions.client.ClientCreateException;
import com.sbt.bank.api.exceptions.client.ClientNotFoundException;
import com.sbt.bank.api.models.Account;
import com.sbt.bank.api.models.Client;
import com.sbt.bank.api.models.ClientInfo;
import com.sbt.bank.api.models.ClientModificationHistory;
import com.sbt.bank.api.models.Currency;
import com.sbt.bank.api.models.Gender;
import com.sbt.bank.api.repositories.ClientModificationHistoryRepository;
import com.sbt.bank.api.repositories.ClientRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ClientServiceImplTest {

    @Mock
    private ClientRepository clientRepository;
    @Mock
    private ClientModificationHistoryRepository clientModificationHistoryRepository;
    @InjectMocks
    private ClientServiceImpl clientService;

    @AfterEach
    public void resetMocks() {
        reset(clientRepository);
        reset(clientModificationHistoryRepository);
    }

    @Test
    void findClientById() {
        var clientInfo = new ClientInfo("4515 193232", "Ivan", "Ivanov", 30, "ivanovIvan@gmail.com", Gender.MAN, "Russia, Moscow, Kutuzovsky, 32, 21", "+7(903)777-7777");
        var client = new Client(UUID.fromString("4f9a97c4-8300-11ee-b962-0242ac120010"), clientInfo, LocalDateTime.now(), LocalDateTime.now(), Collections.EMPTY_LIST);
        doReturn(Optional.of(client)).when(clientRepository).findClientById(client.getId());

        var responseEntity = clientService.findClientById(UUID.fromString("4f9a97c4-8300-11ee-b962-0242ac120010"));

        assertNotNull(responseEntity);
        assertEquals(responseEntity, client);
    }

    @Test
    void findClientByIdClientNotFound() {
        doReturn(Optional.empty()).when(clientRepository).findClientById(UUID.fromString("4f9a97c4-8300-11ee-b962-0242ac120099"));
        assertThrows(ClientNotFoundException.class, () -> {
            clientService.findClientById(UUID.fromString("4f9a97c4-8300-11ee-b962-0242ac120099"));
        });
    }

    @Test
    void getAccountsByClientId() {
        var clientInfo = new ClientInfo("4515 193232", "Ivan", "Ivanov", 30, "ivanovIvan@gmail.com", Gender.MAN, "Russia, Moscow, Kutuzovsky, 32, 21", "+7(903)777-7777");
        var client = new Client(UUID.fromString("4f9a97c4-8300-11ee-b962-0242ac120010"), clientInfo, LocalDateTime.now(), LocalDateTime.now(), Collections.EMPTY_LIST);
        var account1 = new Account(UUID.randomUUID(), "12345678910111213111", Currency.RUR, BigDecimal.valueOf(100), false, new Client());
        var account2 = new Account(UUID.randomUUID(), "12345678910111213112", Currency.RUR, BigDecimal.valueOf(100), false, new Client());
        var account3 = new Account(UUID.randomUUID(), "12345678910111213113", Currency.RUR, BigDecimal.valueOf(100), false, new Client());
        var accounts = List.of(account1, account2, account3);
        client.setAccounts(accounts);

        doReturn(Optional.of(client)).when(clientRepository).findClientById(client.getId());

        var responseEntity = clientService.getAccountsByClientId(UUID.fromString("4f9a97c4-8300-11ee-b962-0242ac120010"));

        assertNotNull(responseEntity);
        assertEquals(responseEntity, accounts);
    }

    @Test
    void getAccountsByClientIdClientNotFound() {
        var clientInfo = new ClientInfo("4515 193232", "Ivan", "Ivanov", 30, "ivanovIvan@gmail.com", Gender.MAN, "Russia, Moscow, Kutuzovsky, 32, 21", "+7(903)777-7777");
        var client = new Client(UUID.fromString("4f9a97c4-8300-11ee-b962-0242ac120099"), clientInfo, LocalDateTime.now(), LocalDateTime.now(), Collections.EMPTY_LIST);
        var account1 = new Account(UUID.randomUUID(), "12345678910111213111", Currency.RUR, BigDecimal.valueOf(100), false, new Client());
        var account2 = new Account(UUID.randomUUID(), "12345678910111213112", Currency.RUR, BigDecimal.valueOf(100), false, new Client());
        var account3 = new Account(UUID.randomUUID(), "12345678910111213113", Currency.RUR, BigDecimal.valueOf(100), false, new Client());
        var accounts = List.of(account1, account2, account3);
        client.setAccounts(accounts);
        doReturn(Optional.empty()).when(clientRepository).findClientById(UUID.fromString("4f9a97c4-8300-11ee-b962-0242ac120099"));
        assertThrows(ClientNotFoundException.class, () -> {
            clientService.findClientById(UUID.fromString("4f9a97c4-8300-11ee-b962-0242ac120099"));
        });
    }

    @Test
    void createClient() {
        var clientInfo = new ClientInfo("4515 193232", "Ivan", "Ivanov", 30, "ivanovIvan@gmail.com", Gender.MAN, "Russia, Moscow, Kutuzovsky, 32, 21", "+7(903)777-7777");
        var client = new Client(UUID.fromString("4f9a97c4-8300-11ee-b962-0242ac120099"), clientInfo, LocalDateTime.now(), LocalDateTime.now(), Collections.EMPTY_LIST);
        var clientDTO = new ClientDTO(clientInfo);

        doReturn(client).when(clientRepository).save(any(Client.class));

        var responseEntity = clientService.createClient(clientDTO);

        assertNotNull(responseEntity);
        assertEquals(responseEntity, client);
    }

    @Test
    void createClientCreateException() {
        var clientInfo = new ClientInfo("4515 193232", "Ivan", "Ivanov", 30, "ivanovIvan@gmail.com", Gender.MAN, "Russia, Moscow, Kutuzovsky, 32, 21", "+7(903)777-7777");
        var clientDTO = new ClientDTO(clientInfo);

        doReturn(Optional.of("4515 193232")).when(clientRepository).findClientByClientInfoPersonalID(clientDTO.clientInfo().getPersonalID());

        assertThrows(ClientCreateException.class, () -> {
            clientService.createClient(clientDTO);
        });
    }

    @Test
    void updateClientPersonalInformation() {
        var clientInfo = new ClientInfo("4515 193232", "Ivan", "Ivanov", 30, "ivanovIvan@gmail.com", Gender.MAN, "Russia, Moscow, Kutuzovsky, 32, 21", "+7(903)777-7777");
        var newClientInfo = new ClientInfo("4515 193232", "Ivan", "Ivanov", 35, "ivanov@gmail.com", Gender.MAN, "Russia, Moscow, Kutuzovsky, 32, 21", "+7(903)777-7777");
        var client = new Client(UUID.fromString("4f9a97c4-8300-11ee-b962-0242ac120099"), clientInfo, LocalDateTime.now(), LocalDateTime.now(), Collections.EMPTY_LIST);
        var clientDTO = new ClientDTO(newClientInfo);
        var clientHistory = new ClientModificationHistory(1L, client.getId(), clientInfo, LocalDateTime.now());

        doReturn(Optional.of(client)).when(clientRepository).findClientById(UUID.fromString("4f9a97c4-8300-11ee-b962-0242ac120099"));
        doReturn(Optional.empty()).when(clientRepository).findClientByClientInfoPersonalID(anyString());
        doReturn(clientHistory).when(clientModificationHistoryRepository).save(any(ClientModificationHistory.class));
        doReturn(client).when(clientRepository).save(client);

        var responseEntity = clientService.updateClientPersonalInformation(UUID.fromString("4f9a97c4-8300-11ee-b962-0242ac120099"), clientDTO);

        assertNotNull(responseEntity);
        assertEquals(responseEntity.getClientInfo(), newClientInfo);
        verify(clientModificationHistoryRepository, times(1)).save(any(ClientModificationHistory.class));
    }

    @Test
    void updateClientPersonalInformationClientNotFound() {

        doReturn(Optional.empty()).when(clientRepository).findClientById(UUID.fromString("4f9a97c4-8300-11ee-b962-0242ac120099"));

        assertThrows(ClientNotFoundException.class, () -> {
            clientService.updateClientPersonalInformation(UUID.fromString("4f9a97c4-8300-11ee-b962-0242ac120099"), new ClientDTO(null));
        });
        verify(clientModificationHistoryRepository, times(0)).save(any(ClientModificationHistory.class));
        verify(clientRepository, times(0)).save(any(Client.class));
        verify(clientRepository, times(0)).findClientByClientInfoPersonalID(anyString());
        verify(clientRepository, times(1)).findClientById(UUID.fromString("4f9a97c4-8300-11ee-b962-0242ac120099"));
    }

    @Test
    void updateClientPersonalInformationClientCreateException() {
        var clientInfo = new ClientInfo("4515 193231", "Ivan", "Ivanov", 30, "ivanovIvan@gmail.com", Gender.MAN, "Russia, Moscow, Kutuzovsky, 32, 21", "+7(903)777-7777");
        var client = new Client(UUID.fromString("4f9a97c4-8300-11ee-b962-0242ac120099"), clientInfo, LocalDateTime.now(), LocalDateTime.now(), Collections.EMPTY_LIST);
        var newClientInfo = new ClientInfo("4515 193232", "Ivan", "Ivanov", 35, "ivanov@gmail.com", Gender.MAN, "Russia, Moscow, Kutuzovsky, 32, 21", "+7(903)777-7777");
        var anotherClient = new Client(UUID.fromString("4f9a97c4-8300-11ee-b962-0242ac120000"), newClientInfo, LocalDateTime.now(), LocalDateTime.now(), Collections.EMPTY_LIST);
        var clientDTO = new ClientDTO(newClientInfo);

        doReturn(Optional.of(client)).when(clientRepository).findClientById(UUID.fromString("4f9a97c4-8300-11ee-b962-0242ac120099"));
        doReturn(Optional.of(anotherClient)).when(clientRepository).findClientByClientInfoPersonalID("4515 193232");

        assertThrows(ClientCreateException.class, () -> {
            clientService.updateClientPersonalInformation(UUID.fromString("4f9a97c4-8300-11ee-b962-0242ac120099"), clientDTO);
        });
        verify(clientModificationHistoryRepository, times(0)).save(any(ClientModificationHistory.class));
        verify(clientRepository, times(0)).save(any(Client.class));
        verify(clientRepository, times(1)).findClientByClientInfoPersonalID(anyString());
        verify(clientRepository, times(1)).findClientById(UUID.fromString("4f9a97c4-8300-11ee-b962-0242ac120099"));
    }
}