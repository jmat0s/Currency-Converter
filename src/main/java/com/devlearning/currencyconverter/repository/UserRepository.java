package com.devlearning.currencyconverter.repository;

import com.devlearning.currencyconverter.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // O Spring cria o SQL automaticamente: "SELECT * FROM tb_users WHERE username = ?"
    Optional<User> findByUsername(String username);
}