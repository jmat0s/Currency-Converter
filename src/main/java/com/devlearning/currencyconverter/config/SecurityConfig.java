package com.devlearning.currencyconverter.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // 1. Autorizar requisições
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/h2-console/**").permitAll() // Libera o H2 Console
                .anyRequest().authenticated() // Exige login para todo o resto
            )
            // 2. Desabilitar proteção CSRF apenas para o H2
            .csrf(csrf -> csrf
                .ignoringRequestMatchers("/h2-console/**")
            )
            // 3. Permitir Frames (necessário para o H2 Console funcionar)
            .headers(headers -> headers
                .frameOptions(frame -> frame.sameOrigin())
            )
            // 4. Habilitar Login Básico (pop-up do navegador) para a API
            .httpBasic(withDefaults());

        return http.build();
    }
}