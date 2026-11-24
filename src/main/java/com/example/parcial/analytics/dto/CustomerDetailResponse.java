package com.example.parcial.analytics.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;
import java.util.List;
import java.util.Map;

@Schema(description = "Cliente con sus cuentas y tiers resueltos")
public record CustomerDetailResponse(
        @Schema(example = "fmiller") String username,
        @Schema(example = "Elizabeth Ray") String name,
        @Schema(example = "9286 Bethany Glens, Vasqueztown, CO 22939") String address,
        Instant birthdate,
        @Schema(example = "arroyocolton@gmail.com") String email,
        @Schema(example = "true") Boolean active,
        List<AccountResponse> accounts,
        Map<String, TierResponse> tierAndDetails
) {
}
