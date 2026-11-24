package com.example.parcial.analytics.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "Cuenta de un cliente")
public record AccountResponse(
        @Schema(example = "371138") Integer accountId,
        @Schema(example = "9000") Integer limit,
        @Schema(example = "[\"Derivatives\", \"InvestmentStock\"]") List<String> products
) {
}
