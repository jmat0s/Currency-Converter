package com.devlearning.currencyconverter.controller;

import com.devlearning.currencyconverter.dto.RegisterRequest;
import com.devlearning.currencyconverter.model.User;
import com.devlearning.currencyconverter.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody RegisterRequest request) {
        
        // 1. Validar se o usuário já existe
        if (userRepository.findByUsername(request.username()).isPresent()) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", "Erro: Username já está em uso!"));
        }

        // 2. Criar o novo usuário
        User newUser = new User();
        newUser.setUsername(request.username());
        
        // 3. IMPORTANTE: Encriptar a senha antes de salvar
        newUser.setPassword(passwordEncoder.encode(request.password()));
        
        // 4. Definir permissão padrão (ninguém vira ADMIN sozinho)
        newUser.setRole("USER");

        userRepository.save(newUser);

        return ResponseEntity.ok(Map.of("message", "Usuário registado com sucesso!"));
    }
}