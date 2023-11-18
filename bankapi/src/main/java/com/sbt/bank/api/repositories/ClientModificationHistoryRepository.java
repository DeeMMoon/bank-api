package com.sbt.bank.api.repositories;

import com.sbt.bank.api.models.ClientModificationHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ClientModificationHistoryRepository extends JpaRepository<ClientModificationHistory, UUID> {
}
