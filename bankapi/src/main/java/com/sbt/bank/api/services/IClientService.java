package com.sbt.bank.api.services;

import com.sbt.bank.api.dto.ClientDTO;
import com.sbt.bank.api.models.Account;
import com.sbt.bank.api.models.Client;
import com.sbt.bank.api.models.ClientInfo;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IClientService {

    Optional<Client> findClientById(UUID id);
    Optional<Client> findClientByClientInfoPersonalID(String id);
    List<Account> getAccountsByClientId(UUID uuid);
    Client createClient(ClientDTO clientDTO);
    Client updateClientPersonalInformation(UUID clientId, ClientDTO clientDTO);
}
