package com.example.parcial.analytics.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.Instant;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Movement {

    private Instant date;
    private Integer amount;

    @Field("transaction_code")
    private String transactionCode;

    private String symbol;
    private String price;
    private String total;
}
