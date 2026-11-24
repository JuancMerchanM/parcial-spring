package com.example.parcial.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuracion global de Swagger.
 * UI: http://localhost:8080/swagger-ui.html
 * JSON: http://localhost:8080/v3/api-docs
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI parcialOpenAPI() {
        return new OpenAPI().info(new Info()
                .title("API sample_analytics")
                .description("Clientes, cuentas y movimientos de la base sample_analytics")
                .version("1.0.0"));
    }
}
