package com.example.parcial.analytics.repository;

import com.example.parcial.analytics.model.TransactionDoc;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Igual que con AccountRepository, se usa List por el account_id duplicado.
 */
@Repository
public interface TransactionRepository extends MongoRepository<TransactionDoc, String> {

    List<TransactionDoc> findByAccountId(Integer accountId);
}
