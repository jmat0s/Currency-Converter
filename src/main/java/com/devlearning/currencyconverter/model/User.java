package com.devlearning.currencyconverter.model;

import jakarta.persistence.*;

@Entity
@Table(name = "tb_users") // Evita conflito com a palavra reservada 'user' do SQL
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password; // A senha será guardada criptografada (Hash)

    // Para simplificar, vamos deixar fixo como "ROLE_USER" por enquanto,
    // mas poderia ser uma lista de roles.
    private String role;

    public User() {}

    public User(String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    // Getters e Setters
    public Long getId() { return id; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
}