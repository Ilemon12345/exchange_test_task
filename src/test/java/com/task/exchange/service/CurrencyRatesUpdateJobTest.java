package com.task.exchange.service;

import com.task.exchange.TestData;
import com.task.exchange.model.Currency;
import com.task.exchange.model.Rate;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static com.task.exchange.TestData.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CurrencyRatesUpdateJobTest {

    @Mock
    CurrencyService currencyService;

    @InjectMocks
    CurrencyRatesUpdateJob currencyRatesUpdateJob;

    @Test
    void shouldUpdateRatesForAllCurrencies() {
        when(currencyService.getAllSavedCurrencies()).thenReturn(List.of(CURRENCY));
        when(currencyService.getLatestProvidersRates(CURRENCY))
                .thenReturn(List.of(Rate.builder().symbol(SYMBOL).rate(SYMBOL_RATE).build()));

        currencyRatesUpdateJob.updateCurrencyRates();

        verify(currencyService).addCurrencies(
                List.of(
                        Currency.builder()
                        .currency(CURRENCY)
                        .rates(
                                List.of(Rate.builder()
                                        .symbol(SYMBOL)
                                        .rate(SYMBOL_RATE)
                                        .build()))
                .build()));

        verify(currencyService).getAllSavedCurrencies();
        verify(currencyService).getLatestProvidersRates(CURRENCY);

        verifyNoMoreInteractions(currencyService);
    }

}
