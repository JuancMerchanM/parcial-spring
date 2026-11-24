package com.example.parcial.cliente;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entidad JPA (base de datos SQL).
 *
 * - @Entity: la clase representa una tabla.
 * - @Table: nombre de la tabla.
 * - @Id + @GeneratedValue: clave primaria autoincremental.
 *
 * Se usa con spring-boot-starter-data-jpa (en este ejemplo, H2 en memoria).
 * Si el parcial pide Mongo, NO uses esta clase: usa @Document como Producto.
 */
@Entity
@Table(name = "clientes")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false, unique = true)
    private String email;
}
