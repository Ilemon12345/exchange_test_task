package com.task.exchange.it;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.task.exchange.TestData;
import com.task.exchange.TestcontainersConfiguration;
import com.task.exchange.endpoints.rest.dto.CurrencyRequest;
import com.task.exchange.endpoints.rest.dto.RateResponse;
import com.task.exchange.integration.provider.FixerRatesProvider;
import com.task.exchange.model.Rate;
import com.task.exchange.service.CurrencyRatesUpdateJob;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static com.task.exchange.TestData.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(TestcontainersConfiguration.class)
@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ExchangeCurrencyEndpointIt {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    CurrencyRatesUpdateJob currencyRatesUpdateJob;

    @MockBean
    public FixerRatesProvider fixerRatesProvider;

    @Test
    void shouldReturnCurrenciesAfterAddingCurrency() throws Exception {
        mockMvc.perform(post("/v1/exchange/currency")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(new CurrencyRequest(CURRENCY))))
                .andExpect(status().isOk());

        var result = mockMvc.perform(get("/v1/exchange/currency")
                        .contentType("application/json"))
                .andExpect(status().isOk()).andReturn();

        assertEquals("[\"EUR\"]", result.getResponse().getContentAsString());
    }

    @Test
    void shouldReturnRatesAfterAddingCurrency() throws Exception {
        when(fixerRatesProvider.getRates(CURRENCY))
                .thenReturn(List.of(
                        Rate.builder()
                                .rate(SYMBOL_RATE)
                                .symbol(SYMBOL)
                                .build())
                );

        mockMvc.perform(post("/v1/exchange/currency")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(new CurrencyRequest(CURRENCY))))
                .andExpect(status().isOk());

        var result = mockMvc.perform(get("/v1/exchange/currency/rates")
                        .contentType("application/json")
                        .param("currency", CURRENCY))
                .andExpect(status().isOk()).andReturn();

        assertEquals(List.of(new RateResponse(SYMBOL, SYMBOL_RATE)),
                objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<List<RateResponse>>() {}));
    }


    @Test
    void shouldReturnRatesAfterJobUpdate() throws Exception {
        when(fixerRatesProvider.getRates(CURRENCY))
                .thenReturn(List.of(
                        Rate.builder()
                                .rate(SYMBOL_RATE)
                                .symbol(SYMBOL)
                                .build())
                ).thenReturn(List.of(
                        Rate.builder()
                                .rate(SYMBOL_RATE_AFTER_SYNC)
                                .symbol(SYMBOL)
                                .build()));

        mockMvc.perform(post("/v1/exchange/currency")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(new CurrencyRequest(CURRENCY))))
                .andExpect(status().isOk());

        currencyRatesUpdateJob.updateCurrencyRates();

        var result = mockMvc.perform(get("/v1/exchange/currency/rates")
                        .contentType("application/json")
                        .param("currency", CURRENCY))
                .andExpect(status().isOk()).andReturn();

        assertEquals(List.of(new RateResponse(SYMBOL, SYMBOL_RATE_AFTER_SYNC)),
                objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<List<RateResponse>>() {}));
    }
}
