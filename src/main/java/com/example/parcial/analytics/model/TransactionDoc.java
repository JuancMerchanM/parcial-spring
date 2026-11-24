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

/**
 * Documento de la coleccion "transactions".
 *
 * Hay un documento por cuenta y los movimientos van embebidos en el array
 * "transactions". No es una coleccion de movimientos individuales.
 */
@Document(collection = "transactions")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionDoc {

    @Id
    private String id;

    @Field("account_id")
    private Integer accountId;

    @Field("transaction_count")
    private Integer transactionCount;

    @Field("bucket_start_date")
    private Instant bucketStartDate;

    @Field("bucket_end_date")
    private Instant bucketEndDate;

    private List<Movement> transactions;
}
