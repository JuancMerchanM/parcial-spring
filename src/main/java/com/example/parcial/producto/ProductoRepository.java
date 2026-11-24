package com.example.parcial.producto;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository de MongoDB.
 *
 * Solo con extender MongoRepository ya se tienen:
 * save, saveAll, findById, findAll, deleteById, delete, count, existsById...
 *
 * Los metodos extra se generan por convencion de nombre (query methods).
 */
@Repository
public interface ProductoRepository extends MongoRepository<Producto, String> {

    Optional<Producto> findByNombreIgnoreCase(String nombre);

    List<Producto> findByPrecioLessThan(Double precio);

    boolean existsByNombreIgnoreCase(String nombre);
}
