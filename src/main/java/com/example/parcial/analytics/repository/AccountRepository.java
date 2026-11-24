package com.example.parcial.analytics.repository;

import com.example.parcial.analytics.model.Account;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;


@Repository
public interface AccountRepository extends MongoRepository<Account, String> {

    List<Account> findByAccountId(Integer accountId);

    List<Account> findByAccountIdIn(Collection<Integer> accountIds);
}
