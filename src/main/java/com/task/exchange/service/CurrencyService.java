package com.task.exchange.service;

import com.task.exchange.model.Currency;
import com.task.exchange.model.Rate;
import com.task.exchange.repository.CurrencyRepository;
import com.task.exchange.integration.provider.RatesProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CurrencyService {

    private final RatesProvider ratesProvider;

    private final CurrencyRepository currencyRepository;
    private Map<String, Currency> currencyListCache = new ConcurrentHashMap<>();

    public void addCurrency(String currency) {
        if (currencyRepository.existsByCurrency(currency)) {
            throw new RuntimeException("Currency " + currency + " already exists");
        }

        var savedCurrency = currencyRepository.save(Currency.builder()
                .currency(currency)
                .rates(ratesProvider.getRates(currency))
                .build());

        currencyListCache.put(currency, savedCurrency);
    }

    public void addCurrencies(List<Currency> currencies) {
        currencyListCache = currencyRepository.saveAll(currencies)
                .stream().collect(Collectors.toConcurrentMap(Currency::getCurrency, Function.identity()));
    }

    public List<String> getAllCurrencies() {
        return new ArrayList<>(currencyListCache.keySet());
    }

    public List<Rate> getRates(String currency) {
        return Optional.ofNullable(currencyListCache.get(currency))
                .orElseThrow(() -> new RuntimeException("Currency not found for " + currency))
                .getRates();
    }

    List<String> getAllSavedCurrencies() {
        return currencyRepository.findDistinctCurrencies();
    }

    List<Rate> getLatestProvidersRates(String currency) {
        return ratesProvider.getRates(currency);
    }
}
