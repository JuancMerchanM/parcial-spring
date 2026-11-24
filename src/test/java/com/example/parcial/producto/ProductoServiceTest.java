package com.example.parcial.producto;

import com.example.parcial.producto.dto.ProductoRequest;
import com.example.parcial.producto.dto.ProductoResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * Test unitario del servicio (no necesita MongoDB ni levantar Spring).
 */
@ExtendWith(MockitoExtension.class)
class ProductoServiceTest {

    @Mock
    private ProductoRepository repository;

    @InjectMocks
    private ProductoService service;

    @Test
    void crearProductoDevuelveElDtoConId() {
        when(repository.save(any(Producto.class))).thenAnswer(invocation -> {
            Producto guardado = invocation.getArgument(0);
            guardado.setId("abc123");
            return guardado;
        });

        ProductoResponse response = service.crear(new ProductoRequest("Teclado", 45.99, 10));

        assertNotNull(response);
        assertEquals("abc123", response.id());
        assertEquals("Teclado", response.nombre());
        assertEquals(45.99, response.precio());
        assertEquals(10, response.stock());
    }
}
