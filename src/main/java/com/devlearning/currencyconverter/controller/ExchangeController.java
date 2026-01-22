package com.devlearning.currencyconverter.controller;

import com.devlearning.currencyconverter.dto.ConversionRequest;
import com.devlearning.currencyconverter.model.ConversionHistory;
import com.devlearning.currencyconverter.service.ExchangeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * REST Controller responsible for handling currency exchange requests.
 * Exposes endpoints for real-time conversion and retrieving transaction history.
 */
@RestController
@RequestMapping("/api/exchange")
public class ExchangeController {

    private final ExchangeService exchangeService;

    /**
     * Constructor injection for the ExchangeService.
     *
     * @param exchangeService the service containing business logic for currency operations.
     */
    public ExchangeController(ExchangeService exchangeService) {
        this.exchangeService = exchangeService;
    }

    /**
     * Converts a specific amount from one currency to another.
     * <p>
     * Endpoint: POST /api/exchange/convert
     * Expected JSON Body: { "fromCurrency": "USD", "toCurrency": "EUR", "amount": 100 }
     *
     * @param request the DTO containing source currency, target currency, and amount.
     * @return the transaction details including the exchange rate and converted amount.
     */
    @PostMapping("/convert")
    public ResponseEntity<ConversionHistory> convertCurrency(@RequestBody ConversionRequest request) {
        ConversionHistory result = exchangeService.convertCurrency(
                request.fromCurrency(),
                request.toCurrency(),
                request.amount()
        );
        return ResponseEntity.ok(result);
    }

    /**
     * Retrieves the full history of conversion transactions.
     * <p>
     * Endpoint: GET /api/exchange/history
     *
     * @return a list of all past conversions saved in the database.
     */
    @GetMapping("/history")
    public ResponseEntity<List<ConversionHistory>> getHistory() {
        return ResponseEntity.ok(exchangeService.findAllHistoryForCurrentUser());
    }

    /**
     * Simple health check endpoint to verify if the API is up and running.
     *
     * @return a map with the current status.
     */
    @GetMapping("/test")
    public ResponseEntity<Map<String, String>> test() {
        return ResponseEntity.ok(Map.of("status", "API is running correctly!"));
    }
}