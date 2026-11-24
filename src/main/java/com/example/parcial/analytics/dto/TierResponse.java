package com.example.parcial.analytics.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "Tier de un cliente")
public record TierResponse(
        @Schema(example = "Bronze") String tier,
        String id,
        @Schema(example = "true") Boolean active,
        @Schema(example = "[\"sports tickets\"]") List<String> benefits
) {
}
