package com.example.parcial.analytics.repository;

import com.example.parcial.analytics.model.Account;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

/**
 * Se devuelve List y no Optional porque el dataset tiene un account_id duplicado
 * (627788). Con Optional, Spring Data lanzaria IncorrectResultSizeDataAccessException.
 */
@Repository
public interface AccountRepository extends MongoRepository<Account, String> {

    List<Account> findByAccountId(Integer accountId);

    List<Account> findByAccountIdIn(Collection<Integer> accountIds);
}
