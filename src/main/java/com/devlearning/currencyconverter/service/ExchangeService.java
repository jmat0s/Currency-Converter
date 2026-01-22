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

import org.springframework.web.client.HttpClientErrorException;
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

        // --- OTIMIZAÇÃO: Se as moedas forem iguais, não chame a API ---
        if (from.equalsIgnoreCase(to)) {
            // Pegar usuário (código repetido, mas necessário aqui)
            String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
            User currentUser = userRepository.findByUsername(currentUsername)
                    .orElseThrow(() -> new RuntimeException("Usuário não encontrado!"));

            ConversionHistory sameCurrencyTransaction = new ConversionHistory(
                    currentUser,
                    from,
                    to,
                    amount,
                    amount, // Convertido é igual ao original
                    BigDecimal.ONE // Taxa é 1.0
            );
            return repository.save(sameCurrencyTransaction);
        }

        // 1. Build the External API URL
        String url = apiUrl + apiKey + "/pair/" + from + "/" + to;

        ExchangeRateResponse response;

        // --- INÍCIO DO TRY-CATCH ---
        try {
            // Tenta chamar a API. Se a moeda for "ZZZ", a API devolve erro 404 e o código pula para o CATCH.
            response = restTemplate.getForObject(url, ExchangeRateResponse.class);
            
        } catch (HttpClientErrorException e) {
            // Captura erros 4xx (ex: 404 Not Found se a moeda não existir)
            throw new IllegalArgumentException("Moeda inválida ou não suportada: " + from + " ou " + to);
            
        } catch (Exception e) {
            // Captura qualquer outro erro (ex: Sem internet, API fora do ar)
            throw new RuntimeException("Erro ao comunicar com o serviço de câmbio.");
        }
        // --- FIM DO TRY-CATCH ---

        // 3. Validate the response (Segurança extra caso a API responda 200 OK mas com erro no corpo)
        if (response == null || !"success".equals(response.result())) {
            throw new RuntimeException("Falha ao obter dados da API externa.");
        }

        // 4. Perform the Calculation
        BigDecimal rate = response.conversionRate();
        BigDecimal convertedAmount = amount.multiply(rate);

        // Pega o usuário logado
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        User currentUser = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado!"));

        // 5. Create the Entity
        ConversionHistory transaction = new ConversionHistory(
                currentUser,
                from,
                to,
                amount,
                convertedAmount,
                rate
        );

        // 6. Save and return
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