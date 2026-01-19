package com.devlearning.currencyconverter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main entry point for the Currency Converter API.
 * <p>
 * The {@link SpringBootApplication} annotation is a convenience annotation that adds all of the following:
 * 1. @Configuration: Tags the class as a source of bean definitions for the application context.
 * 2. @EnableAutoConfiguration: Tells Spring Boot to start adding beans based on classpath settings (like H2, Web, etc).
 * 3. @ComponentScan: Tells Spring to look for other components, configurations, and services in the 'com.devlearning' package.
 */
@SpringBootApplication
public class CurrencyConverterApiApplication {

    /**
     * The main method that bootstraps the Spring application.
     * <p>
     * It launches the application, starts the embedded Tomcat server,
     * and initializes the Spring Context.
     *
     * @param args Command line arguments passed to the application.
     */
    public static void main(String[] args) {
        SpringApplication.run(CurrencyConverterApiApplication.class, args);
    }

}