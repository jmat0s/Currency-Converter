package com.devlearning.currencyconverter.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * Configuration class for application-wide beans.
 * This class serves as a central place to define beans that will be managed by the Spring container.
 */
@Configuration
public class AppConfig {

    /**
     * Creates a RestTemplate bean.
     * * RestTemplate is a synchronous client to perform HTTP requests.
     * We inject this bean into our Service to communicate with the external ExchangeRate API.
     *
     * @return a new instance of RestTemplate
     */
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}