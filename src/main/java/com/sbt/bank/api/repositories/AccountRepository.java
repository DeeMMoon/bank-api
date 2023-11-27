package com.sbt.bank.api.repositories;

import com.sbt.bank.api.models.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AccountRepository extends JpaRepository<Account, UUID> {

    Optional<Account> findAccountByAccountNumber(String accountNumber);
}
