package com.example.parcial.analytics.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Resumen de un cliente")
public record CustomerResponse(
        @Schema(example = "fmiller") String username,
        @Schema(example = "Elizabeth Ray") String name,
        @Schema(example = "arroyocolton@gmail.com") String email,
        @Schema(example = "true") Boolean active,
        @Schema(description = "Cantidad de cuentas asociadas", example = "6") Integer accountsCount
) {
}
