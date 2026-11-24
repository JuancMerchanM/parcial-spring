package com.example.parcial.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuracion global de Swagger.
 * La UI queda en: http://localhost:8080/swagger-ui.html
 * El JSON OpenAPI en: http://localhost:8080/v3/api-docs
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI parcialOpenAPI() {
        return new OpenAPI().info(new Info()
                .title("API Parcial Spring Boot")
                .description("Esquema basico: MongoDB, JPA, DTO, Lombok y Swagger")
                .version("1.0.0"));
    }
}
