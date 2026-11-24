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
