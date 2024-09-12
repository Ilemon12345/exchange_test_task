package com.task.exchange.service;

import com.task.exchange.model.Currency;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CurrencyRatesUpdateJob {

    private final CurrencyService currencyService;

    @Scheduled(fixedRate = 60 * 1000 * 24)
    public void updateCurrencyRates() {
        log.info("Starting updating currency rates");

        var updatedCurrencies = currencyService.getAllSavedCurrencies()
                .parallelStream()
                .map(c -> Currency.builder()
                        .currency(c)
                        .rates(currencyService.getLatestProvidersRates(c))
                        .build())
                .toList();

        currencyService.addCurrencies(updatedCurrencies);

        log.info("Finished updating currency rates");
    }
}
