package com.example.parcial.cliente;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository JPA (SQL).
 *
 * JpaRepository ya trae: save, findAll, findById, deleteById, existsById, count...
 * Tambien se pueden escribir consultas con @Query (JPQL o SQL nativo).
 */
@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    Optional<Cliente> findByEmail(String email);

    boolean existsByEmail(String email);
}
