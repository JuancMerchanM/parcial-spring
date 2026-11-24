package com.example.parcial.producto;

import com.example.parcial.common.ResourceNotFoundException;
import com.example.parcial.producto.dto.ProductoRequest;
import com.example.parcial.producto.dto.ProductoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Capa de servicio: aca va la logica de negocio.
 * El controller NO debe hablar directamente con el repository.
 */
@Service
@RequiredArgsConstructor // Lombok genera el constructor con los campos final (inyeccion por constructor)
public class ProductoService {

    private final ProductoRepository repository;

    public List<ProductoResponse> listar() {
        return repository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public ProductoResponse buscarPorId(String id) {
        Producto producto = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con id " + id));
        return toResponse(producto);
    }

    public ProductoResponse crear(ProductoRequest request) {
        Producto producto = Producto.builder()
                .nombre(request.nombre())
                .precio(request.precio())
                .stock(request.stock())
                .build();
        return toResponse(repository.save(producto));
    }

    public ProductoResponse actualizar(String id, ProductoRequest request) {
        Producto producto = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con id " + id));

        producto.setNombre(request.nombre());
        producto.setPrecio(request.precio());
        producto.setStock(request.stock());
        return toResponse(repository.save(producto));
    }

    public void eliminar(String id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Producto no encontrado con id " + id);
        }
        repository.deleteById(id);
    }

    // Mapper manual: entidad -> DTO de salida
    private ProductoResponse toResponse(Producto producto) {
        return new ProductoResponse(
                producto.getId(),
                producto.getNombre(),
                producto.getPrecio(),
                producto.getStock()
        );
    }
}
