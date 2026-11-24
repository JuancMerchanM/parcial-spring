package com.example.parcial.analytics.controller;

import com.example.parcial.analytics.dto.AccountDetailResponse;
import com.example.parcial.analytics.dto.CustomerDetailResponse;
import com.example.parcial.analytics.dto.CustomerResponse;
import com.example.parcial.analytics.dto.MovementResponse;
import com.example.parcial.analytics.service.AnalyticsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Endpoints de solo lectura sobre sample_analytics.
 */
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "Analytics", description = "Clientes, cuentas y movimientos de sample_analytics")
public class AnalyticsController {

    private final AnalyticsService service;

    @GetMapping("/clientes")
    @Operation(summary = "Listar clientes", description = "Devuelve un resumen de los 500 clientes")
    public ResponseEntity<List<CustomerResponse>> listarClientes() {
        return ResponseEntity.ok(service.listCustomers());
    }

    @GetMapping("/clientes/{username}")
    @Operation(summary = "Buscar cliente por username",
            description = "Devuelve el cliente, sus cuentas y sus tiers")
    @ApiResponse(responseCode = "200", description = "Cliente encontrado")
    @ApiResponse(responseCode = "404", description = "Cliente no encontrado")
    public ResponseEntity<CustomerDetailResponse> buscarCliente(
            @Parameter(description = "Username del cliente", example = "fmiller")
            @PathVariable String username) {
        return ResponseEntity.ok(service.getCustomer(username));
    }

    @GetMapping("/cuentas/{accountId}")
    @Operation(summary = "Buscar cuenta por account_id",
            description = "Devuelve la cuenta y el resumen de sus transacciones")
    @ApiResponse(responseCode = "200", description = "Cuenta encontrada")
    @ApiResponse(responseCode = "404", description = "Cuenta no encontrada")
    public ResponseEntity<AccountDetailResponse> buscarCuenta(
            @Parameter(description = "Identificador de negocio de la cuenta", example = "371138")
            @PathVariable Integer accountId) {
        return ResponseEntity.ok(service.getAccount(accountId));
    }

    @GetMapping("/cuentas/{accountId}/movimientos")
    @Operation(summary = "Listar movimientos de una cuenta",
            description = "Devuelve los movimientos embebidos ordenados por fecha descendente")
    @ApiResponse(responseCode = "200", description = "Cuenta encontrada")
    @ApiResponse(responseCode = "404", description = "Cuenta no encontrada")
    public ResponseEntity<List<MovementResponse>> listarMovimientos(
            @Parameter(description = "Identificador de negocio de la cuenta", example = "371138")
            @PathVariable Integer accountId) {
        return ResponseEntity.ok(service.getMovements(accountId));
    }
}
