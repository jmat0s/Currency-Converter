package com.devlearning.currencyconverter.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;

/**
 * Data Transfer Object (DTO) to map the JSON response from the external ExchangeRate-API.
 * <p>
 * This record acts as a filter, ignoring unnecessary fields from the large external API response
 * and mapping only the specific values needed for our application logic.
 *
 * @param result           The status of the request (e.g., "success").
 * @param conversionRate   The single unit exchange rate between the two currencies.
 * Mapped from the JSON field "conversion_rate" (snake_case) to Java naming convention.
 * @param conversionResult The final calculated amount returned by the API (if applicable).
 * Mapped from the JSON field "conversion_result".
 */
public record ExchangeRateResponse(
    String result,
    
    @JsonProperty("conversion_rate")
    BigDecimal conversionRate,
    
    @JsonProperty("conversion_result")
    BigDecimal conversionResult
) {}