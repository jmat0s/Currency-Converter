package com.devlearning.currencyconverter.controller;

import com.devlearning.currencyconverter.dto.ConversionRequest;
import com.devlearning.currencyconverter.model.ConversionHistory;
import com.devlearning.currencyconverter.service.ExchangeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

//import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/exchange")
public class ExchangeController {

    private final ExchangeService exchangeService;

    // Injeção de dependência do Service
    public ExchangeController(ExchangeService exchangeService) {
        this.exchangeService = exchangeService;
    }

    // 1. Endpoint para Converter Moeda
    // POST http://localhost:8080/api/exchange/convert
    // Body: { "fromCurrency": "USD", "toCurrency": "BRL", "amount": 100 }
    @PostMapping("/convert")
    public ResponseEntity<ConversionHistory> convertCurrency(@RequestBody ConversionRequest request) {
        ConversionHistory result = exchangeService.convertCurrency(
                request.fromCurrency(),
                request.toCurrency(),
                request.amount()
        );
        return ResponseEntity.ok(result);
    }

    // 2. Endpoint para Ver o Histórico
    // GET http://localhost:8080/api/exchange/history
    @GetMapping("/history")
    public ResponseEntity<List<ConversionHistory>> getHistory() {
        return ResponseEntity.ok(exchangeService.findAllHistory());
    }

    // 3. Endpoint de Teste Simples (Health Check)
    @GetMapping("/test")
    public ResponseEntity<Map<String, String>> test() {
        return ResponseEntity.ok(Map.of("status", "API is running correctly!"));
    }
}