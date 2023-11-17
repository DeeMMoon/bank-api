package com.sbt.bank.api.services;

import com.sbt.bank.api.exceptions.client.ClientNotFoundException;
import com.sbt.bank.api.models.Account;
import com.sbt.bank.api.models.Client;
import com.sbt.bank.api.repositories.ClientRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class ClientServiceImpl implements IClientService{

    private final ClientRepository clientRepository;

    public ClientServiceImpl(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Override
    public Optional<Client> findClientById(UUID id) {
        return Optional.ofNullable(clientRepository
                .findClientById(id).
                orElseThrow(() -> new ClientNotFoundException("Client with id: " + id + " not found")));
    }

    @Override
    public List<Account> getAccountsByClientId(UUID uuid) {
        return findClientById(uuid).get().getAccounts();
    }
}
