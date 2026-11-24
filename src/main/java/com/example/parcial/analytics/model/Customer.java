package com.example.parcial.analytics.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.Instant;
import java.util.List;
import java.util.Map;


@Document(collection = "customers")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Customer {

    @Id
    private String id;

    private String username;
    private String name;
    private String address;
    private Instant birthdate;
    private String email;
    private Boolean active;

    
    private List<Integer> accounts;

    
    @Field("tier_and_details")
    private Map<String, Tier> tierAndDetails;
}
