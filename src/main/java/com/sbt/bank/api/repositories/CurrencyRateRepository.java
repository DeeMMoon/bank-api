package com.sbt.bank.api.repositories;

import com.sbt.bank.api.models.CurrencyRate;
import com.sbt.bank.api.models.CurrencyRateKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CurrencyRateRepository extends JpaRepository<CurrencyRate, CurrencyRateKey> {
    Optional<CurrencyRate> findCurrencyRateById(CurrencyRateKey key);
}
