package com.example.parcial.cliente;

import com.example.parcial.cliente.dto.ClienteRequest;
import com.example.parcial.cliente.dto.ClienteResponse;
import com.example.parcial.common.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClienteService {

    private final ClienteRepository repository;

    public List<ClienteResponse> listar() {
        return repository.findAll().stream().map(this::toResponse).toList();
    }

    public ClienteResponse buscarPorId(Long id) {
        Cliente cliente = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado con id " + id));
        return toResponse(cliente);
    }

    public ClienteResponse crear(ClienteRequest request) {
        Cliente cliente = Cliente.builder()
                .nombre(request.nombre())
                .email(request.email())
                .build();
        return toResponse(repository.save(cliente));
    }

    private ClienteResponse toResponse(Cliente cliente) {
        return new ClienteResponse(cliente.getId(), cliente.getNombre(), cliente.getEmail());
    }
}
