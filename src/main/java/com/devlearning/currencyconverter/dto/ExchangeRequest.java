package com.devlearning.currencyconverter.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public record ExchangeRequest(
    
    @NotBlank(message = "A moeda de origem é obrigatória")
    @Pattern(regexp = "^[A-Z]{3}$", message = "A moeda deve ter 3 letras maiúsculas (ex: USD)")
    String from,

    @NotBlank(message = "A moeda de destino é obrigatória")
    @Pattern(regexp = "^[A-Z]{3}$", message = "A moeda deve ter 3 letras maiúsculas (ex: EUR)")
    String to,

    @NotNull(message = "O valor não pode ser nulo")
    @DecimalMin(value = "0.01", message = "O valor deve ser maior que zero")
    BigDecimal amount
) {}