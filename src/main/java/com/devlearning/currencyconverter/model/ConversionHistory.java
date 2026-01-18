package com.devlearning.currencyconverter.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "conversion_history")
public class ConversionHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Moeda de origem (ex: USD)
    @Column(nullable = false, length = 3)
    private String fromCurrency;

    // Moeda de destino (ex: EUR)
    @Column(nullable = false, length = 3)
    private String toCurrency;

    // Valor original
    @Column(nullable = false)
    private BigDecimal originalAmount;

    // Valor convertido
    @Column(nullable = false)
    private BigDecimal convertedAmount;

    // Taxa usada na hora da conversão
    private BigDecimal exchangeRate;

    // Data e hora da transação
    private LocalDateTime timestamp;

    // --- Construtores, Getters e Setters (Padrão Java) ---
    
    public ConversionHistory() {}

    public ConversionHistory(String fromCurrency, String toCurrency, BigDecimal originalAmount, BigDecimal convertedAmount, BigDecimal exchangeRate) {
        this.fromCurrency = fromCurrency;
        this.toCurrency = toCurrency;
        this.originalAmount = originalAmount;
        this.convertedAmount = convertedAmount;
        this.exchangeRate = exchangeRate;
        this.timestamp = LocalDateTime.now();
    }

    // Se estiver usando Lombok, pode apagar tudo abaixo e usar @Data lá em cima.
    // Se não, mantenha os Getters/Setters:
    
    public Long getId() { return id; }
    public String getFromCurrency() { return fromCurrency; }
    public void setFromCurrency(String fromCurrency) { this.fromCurrency = fromCurrency; }
    public String getToCurrency() { return toCurrency; }
    public void setToCurrency(String toCurrency) { this.toCurrency = toCurrency; }
    public BigDecimal getOriginalAmount() { return originalAmount; }
    public void setOriginalAmount(BigDecimal originalAmount) { this.originalAmount = originalAmount; }
    public BigDecimal getConvertedAmount() { return convertedAmount; }
    public void setConvertedAmount(BigDecimal convertedAmount) { this.convertedAmount = convertedAmount; }
    public BigDecimal getExchangeRate() { return exchangeRate; }
    public void setExchangeRate(BigDecimal exchangeRate) { this.exchangeRate = exchangeRate; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
}
