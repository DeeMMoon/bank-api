package com.sbt.bank.api.services;

import com.sbt.bank.api.models.Account;
import com.sbt.bank.api.models.Client;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IClientService {

    Optional<Client> findClientById(UUID id);
    List<Account> getAccountsByClientId(UUID uuid);

}
