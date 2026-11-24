package com.example.parcial.producto.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * DTO de salida (lo que la API devuelve).
 * Se usa un record para no exponer la entidad directamente.
 */
@Schema(description = "Producto devuelto por la API")
public record ProductoResponse(
        @Schema(example = "665f1c2a9b1e4a0f8c7d1234")
        String id,
        @Schema(example = "Teclado mecanico")
        String nombre,
        @Schema(example = "45.99")
        Double precio,
        @Schema(example = "10")
        Integer stock
) {
}
