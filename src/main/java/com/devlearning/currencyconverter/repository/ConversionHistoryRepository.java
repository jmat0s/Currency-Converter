package com.devlearning.currencyconverter.repository;

import com.devlearning.currencyconverter.model.ConversionHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for Data Access Layer (DAO).
 * <p>
 * This interface handles communication with the database for the 'ConversionHistory' entity.
 * By extending {@link JpaRepository}, it inherits standard CRUD operations (Create, Read, Update, Delete)
 * automatically, without needing to write raw SQL queries.
 * <p>
 * Key features inherited:
 * - save(entity): Inserts a new record.
 * - findAll(): Retrieves all records.
 * - findById(id): Retrieves a specific record.
 */
@Repository
public interface ConversionHistoryRepository extends JpaRepository<ConversionHistory, Long> {
    // No implementation logic is needed here. 
    // Spring Data JPA generates the implementation code at runtime (Proxy Pattern).
    List<ConversionHistory> findByUserUsername(String username);
}