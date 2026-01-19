package com.devlearning.currencyconverter.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

/**
 * Security configuration class.
 * This class defines the authentication and authorization rules for the application.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * Configures the Security Filter Chain.
     * Defines which endpoints are public, which require authentication, and handles
     * specific settings for the H2 Database Console.
     *
     * @param http the HttpSecurity configuration object
     * @return the built SecurityFilterChain
     * @throws Exception if an error occurs during configuration
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // 1. Authorization Rules
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/h2-console/**").permitAll() // Allow public access to H2 Console
                .anyRequest().authenticated()                  // Require authentication for all other endpoints
            )
            // 2. Disable CSRF (Cross-Site Request Forgery)
            // Required for H2 Console to work and standard for stateless REST APIs
            .csrf(csrf -> csrf.disable())
            
            // 3. Frame Options
            // Required to allow the H2 Console to run inside an HTML frame
            .headers(headers -> headers
                .frameOptions(frame -> frame.sameOrigin())
            )
            
            // 4. Enable Basic Authentication
            // Allows clients (like Postman) to authenticate using Username/Password headers
            .httpBasic(withDefaults());

        return http.build();
    }
}