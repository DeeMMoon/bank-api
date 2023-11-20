package com.sbt.bank.api.services;

import com.sbt.bank.api.dto.TransactionDTO;
import com.sbt.bank.api.models.Transaction;
import com.sbt.bank.api.models.TransactionStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

public interface ITransactionService {
    Optional<Transaction> findTransactionById(UUID id);
    void doTransaction(Transaction transaction);
    Optional<Transaction> updateTransactionStatus(TransactionStatus status, Transaction transaction);
    Transaction createTransaction(TransactionDTO transactionDTO);
}
