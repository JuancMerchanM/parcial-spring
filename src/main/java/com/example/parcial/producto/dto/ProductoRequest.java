package com.example.parcial.producto.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

/**
 * DTO de entrada (lo que el cliente envia en el body).
 *
 * Es un record: Java genera constructor, getters (nombre(), precio()...),
 * equals, hashCode y toString. NO necesita Lombok.
 * Los records son ideales para DTOs porque son inmutables.
 */
@Schema(description = "Datos para crear o actualizar un producto")
public record ProductoRequest(

        @Schema(description = "Nombre unico del producto", example = "Teclado mecanico")
        @NotBlank(message = "El nombre es obligatorio")
        String nombre,

        @Schema(description = "Precio unitario", example = "45.99")
        @NotNull(message = "El precio es obligatorio")
        @Positive(message = "El precio debe ser mayor que 0")
        Double precio,

        @Schema(description = "Unidades disponibles", example = "10")
        @Min(value = 0, message = "El stock no puede ser negativo")
        Integer stock
) {
}
