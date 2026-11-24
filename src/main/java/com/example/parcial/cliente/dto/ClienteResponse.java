package com.example.parcial.cliente.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Cliente devuelto por la API")
public record ClienteResponse(
        @Schema(example = "1")
        Long id,
        @Schema(example = "Ana Perez")
        String nombre,
        @Schema(example = "ana@mail.com")
        String email
) {
}
