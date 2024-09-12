package com.task.exchange.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

import java.util.Map;

@Configuration
public class RatesProvidersConfig {

    @Value("${rates-provider.fixer.url}")
    private String fixerUrl;

    @Bean
    public RestClient fixerRestClient() {
        return RestClient.builder()
                .baseUrl(fixerUrl)
                .build();
    }
}
