package com.example.parcial.analytics.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


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
