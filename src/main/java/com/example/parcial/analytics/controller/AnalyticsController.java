package com.example.parcial.analytics.controller;

import com.example.parcial.analytics.dto.AccountCreateRequest;
import com.example.parcial.analytics.dto.AccountDetailResponse;
import com.example.parcial.analytics.dto.AccountUpdateRequest;
import com.example.parcial.analytics.dto.CustomerCreateRequest;
import com.example.parcial.analytics.dto.CustomerDetailResponse;
import com.example.parcial.analytics.dto.CustomerResponse;
import com.example.parcial.analytics.dto.CustomerUpdateRequest;
import com.example.parcial.analytics.dto.MovementRequest;
import com.example.parcial.analytics.dto.MovementResponse;
import com.example.parcial.analytics.service.AnalyticsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

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

    @PostMapping("/clientes")
    @Operation(summary = "Crear cliente")
    @ApiResponse(responseCode = "201", description = "Cliente creado")
    @ApiResponse(responseCode = "400", description = "Datos invalidos")
    @ApiResponse(responseCode = "409", description = "Username duplicado")
    public ResponseEntity<CustomerDetailResponse> crearCliente(
            @Valid @RequestBody CustomerCreateRequest request) {
        CustomerDetailResponse created = service.createCustomer(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{username}")
                .buildAndExpand(created.username())
                .toUri();
        return ResponseEntity.created(location).body(created);
    }

    @PutMapping("/clientes/{username}")
    @Operation(summary = "Actualizar cliente")
    @ApiResponse(responseCode = "200", description = "Cliente actualizado")
    @ApiResponse(responseCode = "400", description = "Datos invalidos")
    @ApiResponse(responseCode = "404", description = "Cliente no encontrado")
    public ResponseEntity<CustomerDetailResponse> actualizarCliente(
            @Parameter(description = "Username del cliente", example = "fmiller")
            @PathVariable String username,
            @Valid @RequestBody CustomerUpdateRequest request) {
        return ResponseEntity.ok(service.updateCustomer(username, request));
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

    @PostMapping("/cuentas")
    @Operation(summary = "Crear cuenta")
    @ApiResponse(responseCode = "201", description = "Cuenta creada")
    @ApiResponse(responseCode = "400", description = "Datos invalidos")
    @ApiResponse(responseCode = "409", description = "account_id duplicado")
    public ResponseEntity<AccountDetailResponse> crearCuenta(
            @Valid @RequestBody AccountCreateRequest request) {
        AccountDetailResponse created = service.createAccount(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{accountId}")
                .buildAndExpand(created.accountId())
                .toUri();
        return ResponseEntity.created(location).body(created);
    }

    @PutMapping("/cuentas/{accountId}")
    @Operation(summary = "Actualizar cuenta")
    @ApiResponse(responseCode = "200", description = "Cuenta actualizada")
    @ApiResponse(responseCode = "400", description = "Datos invalidos")
    @ApiResponse(responseCode = "404", description = "Cuenta no encontrada")
    @ApiResponse(responseCode = "409", description = "account_id duplicado")
    public ResponseEntity<AccountDetailResponse> actualizarCuenta(
            @Parameter(description = "Identificador de negocio de la cuenta", example = "371138")
            @PathVariable Integer accountId,
            @Valid @RequestBody AccountUpdateRequest request) {
        return ResponseEntity.ok(service.updateAccount(accountId, request));
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

    @PostMapping("/cuentas/{accountId}/movimientos")
    @Operation(summary = "Agregar movimiento",
            description = "Agrega un movimiento y actualiza el contador y las fechas de la cuenta")
    @ApiResponse(responseCode = "201", description = "Movimiento agregado")
    @ApiResponse(responseCode = "400", description = "Datos invalidos")
    @ApiResponse(responseCode = "404", description = "Cuenta no encontrada")
    @ApiResponse(responseCode = "409", description = "account_id duplicado")
    public ResponseEntity<MovementResponse> agregarMovimiento(
            @Parameter(description = "Identificador de negocio de la cuenta", example = "371138")
            @PathVariable Integer accountId,
            @Valid @RequestBody MovementRequest request) {
        MovementResponse created = service.addMovement(accountId, request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().build().toUri();
        return ResponseEntity.created(location).body(created);
    }
}
