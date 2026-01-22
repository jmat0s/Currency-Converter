package com.devlearning.currencyconverter.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.converter.HttpMessageNotReadableException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 1. Trata erros de validação (ex: @NotNull, @DecimalMin)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationErrors(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        
        // Pega cada campo errado e a mensagem que definimos no DTO
        ex.getBindingResult().getFieldErrors().forEach(error -> 
            errors.put(error.getField(), error.getDefaultMessage())
        );
        
        return ResponseEntity.badRequest().body(errors);
    }

    // 2. Trata erros gerais (caso algo imprevisto aconteça)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleGeneralException(Exception ex) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", "Erro interno no servidor");
        errorResponse.put("message", ex.getMessage());
        
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

    // 3. Trata erros de formato (ex: enviar "banana" no campo de valor)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, String>> handleJsonErrors(HttpMessageNotReadableException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", "Formato de JSON inválido");
        // Mensagem amigável explicando o erro
        error.put("details", "Provavelmente você enviou um texto num campo numérico ou o JSON está mal formatado.");
        
        return ResponseEntity.badRequest().body(error);
    }

    // 4. Trata erros de lógica de negócio (ex: Moeda inexistente)
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleBusinessException(IllegalArgumentException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", "Erro de Operação");
        error.put("message", ex.getMessage()); // Vai mostrar "Moeda inválida..."
        
        return ResponseEntity.badRequest().body(error);
    }
}