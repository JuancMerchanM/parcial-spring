package com.example.parcial;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Punto de entrada de la aplicacion.
 * @SpringBootApplication = @Configuration + @EnableAutoConfiguration + @ComponentScan
 */
@SpringBootApplication
public class ParcialApplication {

    public static void main(String[] args) {
        SpringApplication.run(ParcialApplication.class, args);
    }
}
