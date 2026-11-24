package com.example.parcial.analytics.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;
import java.util.List;

@Schema(description = "Cuenta con el resumen de sus transacciones")
public record AccountDetailResponse(
        @Schema(example = "371138") Integer accountId,
        @Schema(example = "9000") Integer limit,
        List<String> products,
        @Schema(description = "Cantidad total de movimientos", example = "66") Integer transactionCount,
        Instant bucketStartDate,
        Instant bucketEndDate
) {
}
