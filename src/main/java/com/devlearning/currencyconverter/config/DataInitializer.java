package com.devlearning.currencyconverter.config;

import com.devlearning.currencyconverter.model.User;
import com.devlearning.currencyconverter.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;



@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner initDatabase(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            // Verifica se já existe um admin, se não, cria um
            if (userRepository.findByUsername("admin").isEmpty()) {
                User admin = new User();
                admin.setUsername("admin");
                // IMPORTANTE: Codificar a senha antes de salvar!
                admin.setPassword(passwordEncoder.encode("admin123")); 
                admin.setRole("ADMIN");
                
                userRepository.save(admin);
                System.out.println(">>> Usuário ADMIN criado com sucesso! (user: admin / pass: admin123)");
            }
        };
    }
}