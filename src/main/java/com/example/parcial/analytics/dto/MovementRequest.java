package com.example.parcial.analytics.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;

import java.time.Instant;

@Schema(description = "Datos para agregar un movimiento")
public record MovementRequest(
        @NotNull(message = "La fecha es obligatoria")
        Instant date,

        @NotNull(message = "El amount es obligatorio")
        @Positive(message = "El amount debe ser positivo")
        Integer amount,

        @NotBlank(message = "El transaction_code es obligatorio")
        @Pattern(regexp = "buy|sell", message = "El transaction_code debe ser buy o sell")
        String transactionCode,

        @NotBlank(message = "El symbol es obligatorio")
        String symbol,

        @NotBlank(message = "El price es obligatorio")
        String price,

        @NotBlank(message = "El total es obligatorio")
        String total
) {
}
