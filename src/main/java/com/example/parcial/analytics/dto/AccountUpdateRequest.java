package com.example.parcial.analytics.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.List;

@Schema(description = "Datos para actualizar una cuenta")
public record AccountUpdateRequest(
        @NotNull(message = "El limite es obligatorio")
        @Positive(message = "El limite debe ser positivo")
        Integer limit,

        List<@NotBlank(message = "El producto no puede estar vacio") String> products
) {
}
