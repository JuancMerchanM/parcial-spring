package com.example.parcial.cliente.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Datos para crear un cliente")
public record ClienteRequest(
        @Schema(example = "Ana Perez")
        @NotBlank(message = "El nombre es obligatorio")
        String nombre,

        @Schema(example = "ana@mail.com")
        @NotBlank(message = "El email es obligatorio")
        @Email(message = "El email no tiene formato valido")
        String email
) {
}
