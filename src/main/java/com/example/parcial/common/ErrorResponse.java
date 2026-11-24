package com.example.parcial.common;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Cuerpo de error uniforme para toda la API.
 * Usar un record evita crear una clase con getters/setters.
 */
public record ErrorResponse(
        LocalDateTime timestamp,
        int status,
        String error,
        String message,
        String path,
        Map<String, String> detalles
) {
    public static ErrorResponse of(int status, String error, String message, String path) {
        return new ErrorResponse(LocalDateTime.now(), status, error, message, path, null);
    }
}
