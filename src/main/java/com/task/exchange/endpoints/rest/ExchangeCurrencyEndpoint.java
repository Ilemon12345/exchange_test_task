package com.task.exchange.endpoints.rest;

import com.task.exchange.endpoints.rest.dto.CurrencyRequest;
import com.task.exchange.endpoints.rest.dto.RateResponse;
import com.task.exchange.service.CurrencyService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/v1/exchange/currency")
public class ExchangeCurrencyEndpoint {

    private final CurrencyService currencyService;

    @GetMapping
    public List<String> getCurencyList() {
        return currencyService.getAllCurrencies();
    }

    @PostMapping
    public void addCurrency(@RequestBody CurrencyRequest currencyRequest) {
        currencyService.addCurrency(currencyRequest.getCurrency());
    }

    @GetMapping("/rates")
    public List<RateResponse> getRates(@RequestParam String currency) {
        return currencyService.getRates(currency)
                .stream()
                .map(rate -> new RateResponse(rate.getSymbol(), rate.getRate()))
                .toList();
    }
}
