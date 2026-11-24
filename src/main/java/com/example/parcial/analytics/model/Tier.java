package com.example.parcial.analytics.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Tier embebido dentro del mapa "tier_and_details" de Customer.
 * La clave del mapa es un UUID y el campo id repite ese UUID.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Tier {

    private String tier;
    private String id;
    private Boolean active;
    private List<String> benefits;
}
