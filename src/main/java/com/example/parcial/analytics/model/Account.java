package com.example.parcial.analytics.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

/**
 * Documento de la coleccion "accounts".
 * El identificador de negocio es account_id (entero), no el _id de Mongo.
 */
@Document(collection = "accounts")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Account {

    @Id
    private String id;

    @Field("account_id")
    private Integer accountId;

    /** Limite de la cuenta. "limit" no es palabra reservada en Java. */
    private Integer limit;

    private List<String> products;
}
