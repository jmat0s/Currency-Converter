package com.devlearning.currencyconverter.dto;

import java.math.BigDecimal;

/**
 * Data Transfer Object (DTO) representing a client's request to convert currency.
 * <p>
 * This record captures the input data sent via the JSON body of the HTTP POST request.
 * using Java Records (introduced in Java 16) ensures immutability and concise syntax.
 *
 * @param fromCurrency The 3-letter ISO code of the source currency (e.g., "USD").
 * @param toCurrency   The 3-letter ISO code of the target currency (e.g., "EUR").
 * @param amount       The monetary value to be converted. 
 * BigDecimal is used instead of Double to ensure financial precision.
 */
public record ConversionRequest(
    String fromCurrency, 
    String toCurrency, 
    BigDecimal amount
) {}