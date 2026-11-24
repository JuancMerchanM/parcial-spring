package com.example.parcial.analytics.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;

@Schema(description = "Movimiento individual de una cuenta")
public record MovementResponse(
        Instant date,
        @Schema(example = "7514") Integer amount,
        @Schema(example = "buy") String transactionCode,
        @Schema(example = "adbe") String symbol,
        @Schema(example = "19.1072802650074180519368383102118968963623046875") String price,
        @Schema(example = "143572.1039112657392422534031") String total
) {
}
