package com.task.exchange.integration.provider;

import com.task.exchange.model.Rate;

import java.util.List;

public interface RatesProvider {

    List<Rate> getRates(String currency);
}
