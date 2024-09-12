package com.task.exchange.repository;

import com.task.exchange.model.Currency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CurrencyRepository extends JpaRepository<Currency, UUID> {

    @Query("select c from Currency c where c.currency = :currency order by c.creationDate DESC LIMIT 1")
    Optional<Currency> findCurrency(String currency);

    @Query("select distinct c.currency from Currency c")
    List<String> findDistinctCurrencies();

    boolean existsByCurrency(String currency);
}
