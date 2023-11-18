package com.sbt.bank.api.services;

import com.sbt.bank.api.dto.ClientDTO;
import com.sbt.bank.api.exceptions.client.ClientCreateException;
import com.sbt.bank.api.exceptions.client.ClientNotFoundException;
import com.sbt.bank.api.models.Account;
import com.sbt.bank.api.models.Client;
import com.sbt.bank.api.models.ClientModificationHistory;
import com.sbt.bank.api.repositories.ClientModificationHistoryRepository;
import com.sbt.bank.api.repositories.ClientRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class ClientServiceImpl implements IClientService{

    private final ClientRepository clientRepository;
    private final ClientModificationHistoryRepository clientModificationHistoryRepository;

    public ClientServiceImpl(ClientRepository clientRepository, ClientModificationHistoryRepository clientModificationHistoryRepository) {
        this.clientRepository = clientRepository;
        this.clientModificationHistoryRepository = clientModificationHistoryRepository;
    }

    @Override
    public Optional<Client> findClientById(UUID id) {
        return Optional.ofNullable(clientRepository
                .findClientById(id).
                orElseThrow(() -> new ClientNotFoundException("Client with id: " + id + " not found")));
    }

    @Override
    public Optional<Client> findClientByClientInfoPersonalID(String id) {
        return clientRepository.findClientByClientInfoPersonalID(id);
    }

    @Override
    public List<Account> getAccountsByClientId(UUID uuid) {
        return findClientById(uuid).get().getAccounts();
    }

    @Override
    @Transactional
    public Client createClient(ClientDTO clientDTO) {
      if (clientRepository.findClientByClientInfoPersonalID(clientDTO.clientInfo().getPersonalID()).isPresent()){
            throw new ClientCreateException("It is not possible to create a profile because a client with such a passport has already been created");
      }
      Client client = new Client().builder()
                .id(UUID.randomUUID())
                .clientInfo(clientDTO.clientInfo())
                .createdTime(LocalDateTime.now())
                .lastModifiedTime(LocalDateTime.now())
                .accounts(Collections.EMPTY_LIST)
                .build();
      return clientRepository.save(client);
    }

    @Override
    @Transactional
    public Client updateClientPersonalInformation(UUID clientId, ClientDTO clientDTO) {
        var client = clientRepository.findClientById(clientId);
        if (client.isEmpty()){
            throw new ClientNotFoundException("Client with id: " + clientId + " not found");
        }
        if (clientRepository.findClientByClientInfoPersonalID(clientDTO.clientInfo().getPersonalID()).isPresent()){
            throw new ClientCreateException("It is not possible to create a profile because a client with such a passport has already been created");
        }
        var clientModificationHistory = new ClientModificationHistory().builder()
                .clientId(client.get().getId())
                .clientInfo(client.get().getClientInfo())
                .modifiedTime(LocalDateTime.now())
                .build();
        clientModificationHistoryRepository.save(clientModificationHistory);
        client.get().setClientInfo(clientDTO.clientInfo());
        client.get().setLastModifiedTime(LocalDateTime.now());
        return client.get();
    }

}
