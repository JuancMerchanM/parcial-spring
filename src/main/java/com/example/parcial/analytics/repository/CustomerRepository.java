package com.example.parcial.analytics.repository;

import com.example.parcial.analytics.model.Customer;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepository extends MongoRepository<Customer, String> {

    Optional<Customer> findByUsername(String username);

    boolean existsByUsername(String username);

    List<Customer> findAllByOrderByUsernameAsc();
}
