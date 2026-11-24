package com.example.parcial.common;

/**
 * Excepcion de negocio para cuando un recurso no existe.
 * El GlobalExceptionHandler la traduce a HTTP 404.
 */
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
