package com.devlearning.currencyconverter.dto;

import java.math.BigDecimal;

public record ConversionRequest(
    String fromCurrency, 
    String toCurrency, 
    BigDecimal amount
) {}