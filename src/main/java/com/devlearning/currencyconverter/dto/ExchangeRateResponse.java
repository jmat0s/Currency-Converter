package com.devlearning.currencyconverter.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;

// Mapeia apenas os campos que nos interessam do JSON da API externa
public record ExchangeRateResponse(
    String result,
    
    @JsonProperty("conversion_rate")
    BigDecimal conversionRate,
    
    @JsonProperty("conversion_result")
    BigDecimal conversionResult
) {}