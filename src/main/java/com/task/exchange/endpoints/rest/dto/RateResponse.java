package com.task.exchange.endpoints.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class RateResponse {
    private String symbol;
    private BigDecimal rate;
}
