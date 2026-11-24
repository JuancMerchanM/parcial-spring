package com.example.parcial.analytics.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;
import java.util.List;

@Schema(description = "Datos para actualizar un cliente")
public record CustomerUpdateRequest(
        @NotBlank(message = "El nombre es obligatorio")
        String name,

        String address,

        Instant birthdate,

        @NotBlank(message = "El email es obligatorio")
        @Email(message = "El email no tiene formato valido")
        String email,

        Boolean active,

        List<@NotNull(message = "El account_id no puede ser nulo") Integer> accounts
) {
}
