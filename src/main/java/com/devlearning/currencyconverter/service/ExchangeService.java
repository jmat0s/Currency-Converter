package com.devlearning.currencyconverter.service;

import com.devlearning.currencyconverter.dto.ExchangeRateResponse;
import com.devlearning.currencyconverter.model.ConversionHistory;
import com.devlearning.currencyconverter.repository.ConversionHistoryRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ExchangeService {

    // Dependências (Ferramentas que vamos usar)
    private final RestTemplate restTemplate;
    private final ConversionHistoryRepository repository;

    // Variáveis que vêm do application.properties
    @Value("${currency.api.url}")
    private String apiUrl;

    @Value("${currency.api.key}")
    private String apiKey;

    // Construtor: O Spring injeta as ferramentas automaticamente aqui
    public ExchangeService(RestTemplate restTemplate, ConversionHistoryRepository repository) {
        this.restTemplate = restTemplate;
        this.repository = repository;
    }

    // --- O Método Principal: Converter ---
    public ConversionHistory convertCurrency(String from, String to, BigDecimal amount) {
        
        // 1. Montar a URL da API Externa
        // Formato da ExchangeRate-API: https://.../v6/CHAVE/pair/USD/EUR
        String url = apiUrl + apiKey + "/pair/" + from + "/" + to;

        // 2. Fazer a chamada externa
        // O RestTemplate vai na URL, pega o JSON e transforma no objeto ExchangeRateResponse
        ExchangeRateResponse response = restTemplate.getForObject(url, ExchangeRateResponse.class);

        // 3. Validar se deu certo
        if (response == null || !"success".equals(response.result())) {
            throw new RuntimeException("Falha ao obter taxas de câmbio da API externa.");
        }

        // 4. A Matemática (Calcular o valor final)
        BigDecimal rate = response.conversionRate();
        BigDecimal convertedAmount = amount.multiply(rate);

        // 5. Criar o registro para o banco (Entidade)
        ConversionHistory transaction = new ConversionHistory(
                from, 
                to, 
                amount, 
                convertedAmount, 
                rate
        );

        // 6. Salvar no Banco (O Arquivista) e retornar
        return repository.save(transaction);
    }

    // Método extra para listar o histórico (usaremos depois)
    public List<ConversionHistory> findAllHistory() {
        return repository.findAll();
    }
}