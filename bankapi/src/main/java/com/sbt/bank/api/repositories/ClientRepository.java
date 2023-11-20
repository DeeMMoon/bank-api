package com.sbt.bank.api.repositories;

import com.sbt.bank.api.models.Client;
import com.sbt.bank.api.models.ClientInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ClientRepository extends JpaRepository<Client, UUID> {
    Optional<Client> findClientById(UUID id);
    Optional<Client> findClientByClientInfoPersonalID(String personalID);
}
