package com.sbt.bank.api.repositories;

import com.sbt.bank.api.models.ClientModificationHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ClientModificationHistoryRepository extends JpaRepository<ClientModificationHistory, UUID> {
}
