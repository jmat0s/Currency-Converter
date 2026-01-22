package com.devlearning.currencyconverter.service;

import com.devlearning.currencyconverter.dto.ExchangeRateResponse;
import com.devlearning.currencyconverter.model.ConversionHistory;
import com.devlearning.currencyconverter.repository.ConversionHistoryRepository;
import com.devlearning.currencyconverter.repository.UserRepository;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.List;

import com.devlearning.currencyconverter.model.User;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
// ...

/**
 * Service layer responsible for business logic regarding currency exchange.
 * <p>
 * This class acts as the bridge between the Controller (API endpoint),
 * the External ExchangeRate API (Real-time data), and the Repository (Database).
 */
@Service
public class ExchangeService {

    // Dependencies
    private final RestTemplate restTemplate;
    private final ConversionHistoryRepository repository;
    private final UserRepository userRepository;

    // Configuration values injected from application.properties
    // This keeps the URL and Key flexible without hardcoding them in Java.
    @Value("${currency.api.url}")
    private String apiUrl;

    @Value("${currency.api.key}")
    private String apiKey;

    /**
     * Constructor Injection.
     * Spring automatically injects the configured RestTemplate and Repository beans.
     * @param restTemplate The utility to make HTTP requests to the external API.
     * @param repository   The Data Access Object to save transactions.
     */
    public ExchangeService(RestTemplate restTemplate, ConversionHistoryRepository repository,UserRepository userRepository) {
        this.restTemplate = restTemplate;
        this.repository = repository;
        this.userRepository = userRepository;
    }

    /**
     * Performs the full currency conversion process.
     * <p>
     * Flow:
     * 1. Constructs the external API URL.
     * 2. Calls the external API to get the current exchange rate.
     * 3. Calculates the converted amount based on the rate.
     * 4. Saves the transaction details to the local database.
     *
     * @param from   Source currency code (e.g., "USD").
     * @param to     Target currency code (e.g., "EUR").
     * @param amount The amount to be converted.
     * @return The saved ConversionHistory entity containing all transaction details.
     */
    public ConversionHistory convertCurrency(String from, String to, BigDecimal amount) {
        
        // 1. Build the External API URL dynamically
        // Pattern: https://v6.exchangerate-api.com/v6/{KEY}/pair/{FROM}/{TO}
        String url = apiUrl + apiKey + "/pair/" + from + "/" + to;

        // 2. Execute the HTTP GET request
        // RestTemplate maps the JSON response directly to our ExchangeRateResponse DTO
        ExchangeRateResponse response = restTemplate.getForObject(url, ExchangeRateResponse.class);

        // 3. Validate the response
        if (response == null || !"success".equals(response.result())) {
            throw new RuntimeException("Failed to fetch exchange rates from external API.");
        }

        // 4. Perform the Calculation (Business Logic)
        BigDecimal rate = response.conversionRate();
        BigDecimal convertedAmount = amount.multiply(rate);

        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();

        User currentUser = userRepository.findByUsername(currentUsername).orElseThrow(() -> new RuntimeException("Usuário não encontrado!"));

        // 5. Create the Entity object to be persisted
        ConversionHistory transaction = new ConversionHistory(
                currentUser,
                from, 
                to, 
                amount, 
                convertedAmount, 
                rate
        );

        // 6. Save to Database and return the result
        return repository.save(transaction);
    }

    /**
     * Retrieves the complete history of all conversions performed.
     *
     * @return A list of all ConversionHistory records.
     */
    public List<ConversionHistory> findAllHistoryForCurrentUser() {
        String currentUserName = SecurityContextHolder.getContext().getAuthentication().getName();
        return repository.findByUserUsername(currentUserName);
    }
}