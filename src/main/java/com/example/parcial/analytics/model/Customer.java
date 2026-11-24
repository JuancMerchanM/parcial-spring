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

/**
 * Documento de la coleccion "customers".
 *
 * Relacion principal: accounts[] guarda los account_id de la coleccion "accounts".
 * No existe una referencia inversa desde Account hacia Customer.
 */
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

    /** account_id de las cuentas del cliente. */
    private List<Integer> accounts;

    /** Mapa UUID -> tier. El nombre real en Mongo es "tier_and_details". */
    @Field("tier_and_details")
    private Map<String, Tier> tierAndDetails;
}
