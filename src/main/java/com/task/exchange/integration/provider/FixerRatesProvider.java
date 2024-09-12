package com.task.exchange.integration.provider;

import com.task.exchange.model.Rate;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FixerRatesProvider implements RatesProvider {

    //fixer.io doesn't support switch base in free plan that's why EUR is used for all cases
    private static final String BASE = "EUR";

    @Value("${rates-provider.fixer.apiKey}")
    private String fixerApiKey;

    private final RestClient fixerRatesRestClient;

    @Override
    public List<Rate> getRates(String currency) {
        return Optional.ofNullable(fixerRatesRestClient.get()
                .uri(uriBuilder -> uriBuilder
                        .queryParam("access_key", fixerApiKey)
                        .queryParam("base", BASE)
                        .build())
                .retrieve()
                .body(FixerRatesResponse.class))
                .orElseThrow(() -> new RuntimeException("Error during getting fixer rates for currency: " + currency))
                .getRates()
                .entrySet()
                .stream()
                .map(res -> Rate.builder()
                        .rate(res.getValue())
                        .symbol(res.getKey())
                        .build())
                .toList();
    }

    @Data
    public static class FixerRatesResponse {
        private Map<String, BigDecimal> rates;
    }

    //only for tests
    void setFixerApiKey(String fixerApiKey) {
        this.fixerApiKey = fixerApiKey;
    }
}
