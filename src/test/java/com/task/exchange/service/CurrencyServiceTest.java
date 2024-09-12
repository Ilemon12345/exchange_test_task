package com.task.exchange.service;

import com.task.exchange.TestData;
import com.task.exchange.integration.provider.RatesProvider;
import com.task.exchange.model.Currency;
import com.task.exchange.model.Rate;
import com.task.exchange.repository.CurrencyRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static com.task.exchange.TestData.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CurrencyServiceTest {

    @Mock
    CurrencyRepository currencyRepository;

    @Mock
    RatesProvider ratesProvider;

    @InjectMocks
    CurrencyService currencyService;

    @Test
    void shouldAddCurrency() {
        var currencyToSave = Currency.builder()
                .currency(CURRENCY)
                .rates(List.of(Rate.builder().rate(SYMBOL_RATE).symbol(SYMBOL).build()))
                .build();

        when(currencyRepository.save(currencyToSave)).thenReturn(currencyToSave);
        when(currencyRepository.existsByCurrency(CURRENCY)).thenReturn(false);
        when(ratesProvider.getRates(CURRENCY)).thenReturn(
                List.of(Rate.builder().rate(SYMBOL_RATE).symbol(SYMBOL).build()));

        currencyService.addCurrency(CURRENCY);

        var currentRates = currencyService.getRates(CURRENCY);

        assertEquals(currencyToSave.getRates(), currentRates);

        verify(currencyRepository).save(currencyToSave);
        verify(currencyRepository).existsByCurrency(CURRENCY);
        verify(ratesProvider).getRates(CURRENCY);

        verifyNoMoreInteractions(currencyRepository, ratesProvider);
    }

    @Test
    void shouldAddCurrencies() {
        var currencyToSave = Currency.builder()
                .currency(CURRENCY)
                .rates(List.of(Rate.builder().rate(SYMBOL_RATE).symbol(SYMBOL).build()))
                .build();

        when(currencyRepository.saveAll(List.of(currencyToSave))).thenReturn(List.of(currencyToSave));

        currencyService.addCurrencies(List.of(currencyToSave));

        var currentRates = currencyService.getRates(CURRENCY);

        assertEquals(currencyToSave.getRates(), currentRates);

        verify(currencyRepository).saveAll(List.of(currencyToSave));

        verifyNoMoreInteractions(currencyRepository, ratesProvider);
    }
}
