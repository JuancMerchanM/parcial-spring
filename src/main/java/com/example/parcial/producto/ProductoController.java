package com.example.parcial.producto;

import com.example.parcial.producto.dto.ProductoRequest;
import com.example.parcial.producto.dto.ProductoResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
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

/**
 * Controller REST del recurso Producto (MongoDB).
 *
 * @RestController = @Controller + @ResponseBody (devuelve JSON automaticamente)
 * @RequestMapping define la ruta base.
 * ResponseEntity permite controlar el codigo HTTP, headers y body.
 */
@RestController
@RequestMapping("/api/productos")
@RequiredArgsConstructor
@Tag(name = "Productos", description = "CRUD de productos en MongoDB")
public class ProductoController {

    private final ProductoService service;

    @GetMapping
    @Operation(summary = "Listar todos los productos")
    public ResponseEntity<List<ProductoResponse>> listar() {
        return ResponseEntity.ok(service.listar()); // 200 OK
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar un producto por id")
    @ApiResponse(responseCode = "200", description = "Producto encontrado")
    @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    public ResponseEntity<ProductoResponse> buscar(@PathVariable String id) {
        return ResponseEntity.ok(service.buscarPorId(id)); // 200 OK
    }

    @PostMapping
    @Operation(summary = "Crear un producto")
    @ApiResponse(responseCode = "201", description = "Producto creado")
    @ApiResponse(responseCode = "400", description = "Datos invalidos")
    public ResponseEntity<ProductoResponse> crear(@Valid @RequestBody ProductoRequest request) {
        ProductoResponse creado = service.crear(request);

        // Header Location: /api/productos/{id}
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(creado.id())
                .toUri();

        return ResponseEntity.created(location).body(creado); // 201 Created
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar un producto")
    public ResponseEntity<ProductoResponse> actualizar(@PathVariable String id,
                                                       @Valid @RequestBody ProductoRequest request) {
        return ResponseEntity.ok(service.actualizar(id, request)); // 200 OK
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un producto")
    @ApiResponse(responseCode = "204", description = "Producto eliminado")
    public ResponseEntity<Void> eliminar(@PathVariable String id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build(); // 204 No Content
    }
}
