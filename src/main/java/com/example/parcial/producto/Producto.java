package com.example.parcial.producto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Entidad de MongoDB.
 *
 * - @Document indica que se guarda en la coleccion "productos".
 * - @Id es el identificador de Mongo (String/ObjectId).
 * - Lombok genera getters, setters, constructor vacio, constructor completo y builder.
 *
 * En Mongo NO se usa @Entity / @Table / @GeneratedValue: eso es de JPA (SQL).
 */
@Document(collection = "productos")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Producto {

    @Id
    private String id;

    @NotBlank(message = "El nombre es obligatorio")
    @Indexed(unique = true)
    private String nombre;

    @NotNull(message = "El precio es obligatorio")
    @Positive(message = "El precio debe ser mayor que 0")
    private Double precio;

    @Min(value = 0, message = "El stock no puede ser negativo")
    private Integer stock;
}
