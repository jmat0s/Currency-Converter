package com.devlearning.currencyconverter.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * JPA Entity representing a record of a currency conversion transaction.
 * <p>
 * This class maps directly to the "conversion_history" table in the database.
 * Each instance of this class corresponds to a single row in that table.
 */
@Entity
@Table(name = "conversion_history")
public class ConversionHistory {

    /**
     * The unique identifier for the transaction (Primary Key).
     * Generated automatically by the database (Auto-Increment).
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The source currency code (e.g., "USD").
     * Stored as a 3-character ISO 4217 string.
     */
    @Column(nullable = false, length = 3)
    private String fromCurrency;

    /**
     * The target currency code (e.g., "EUR").
     * Stored as a 3-character ISO 4217 string.
     */
    @Column(nullable = false, length = 3)
    private String toCurrency;

    /**
     * The initial monetary value before conversion.
     */
    @Column(nullable = false)
    private BigDecimal originalAmount;

    /**
     * The final calculated value after applying the exchange rate.
     */
    @Column(nullable = false)
    private BigDecimal convertedAmount;

    /**
     * The exchange rate used at the specific moment of the transaction.
     * Storing this is crucial for historical accuracy.
     */
    private BigDecimal exchangeRate;

    /**
     * The exact date and time when the conversion occurred.
     */
    private LocalDateTime timestamp;

    // --- Constructors ---

    /**
     * Default constructor required by JPA/Hibernate.
     * Should not be used directly in application logic.
     */
    public ConversionHistory() {}

    /**
     * Custom constructor to create a new transaction record.
     * The timestamp is automatically set to the current time upon creation.
     *
     * @param fromCurrency   Source currency code.
     * @param toCurrency     Target currency code.
     * @param originalAmount The amount the user wanted to convert.
     * @param convertedAmount The result calculated by the service.
     * @param exchangeRate   The rate fetched from the external API.
     */
    public ConversionHistory(String fromCurrency, String toCurrency, BigDecimal originalAmount, BigDecimal convertedAmount, BigDecimal exchangeRate) {
        this.fromCurrency = fromCurrency;
        this.toCurrency = toCurrency;
        this.originalAmount = originalAmount;
        this.convertedAmount = convertedAmount;
        this.exchangeRate = exchangeRate;
        this.timestamp = LocalDateTime.now();
    }

    // --- Getters and Setters ---

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