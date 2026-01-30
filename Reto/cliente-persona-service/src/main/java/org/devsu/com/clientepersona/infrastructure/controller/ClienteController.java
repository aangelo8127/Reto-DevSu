package org.devsu.com.clientepersona.infrastructure.controller;

import lombok.RequiredArgsConstructor;
import org.devsu.com.clientepersona.application.usecase.ClienteService;
import org.devsu.com.clientepersona.domain.entity.Cliente;
import org.devsu.com.clientepersona.infrastructure.dto.ClienteRequest;
import org.devsu.com.clientepersona.infrastructure.dto.ClienteResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/clientes")
@RequiredArgsConstructor
public class ClienteController {

    private final ClienteService clienteService;

    @GetMapping
    public ResponseEntity<List<ClienteResponse>> obtenerTodos() {
        List<ClienteResponse> clientes = clienteService.obtenerTodosLosClientes()
                .stream()
                .map(this::clienteToResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(clientes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClienteResponse> obtenerPorId(@PathVariable Long id) {
        return clienteService.obtenerClientePorId(id)
                .map(cliente -> ResponseEntity.ok(clienteToResponse(cliente)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/identificacion/{clienteId}")
    public ResponseEntity<ClienteResponse> obtenerPorClienteId(@PathVariable String clienteId) {
        return clienteService.obtenerClientePorClienteId(clienteId)
                .map(cliente -> ResponseEntity.ok(clienteToResponse(cliente)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ClienteResponse> crear(@Valid @RequestBody ClienteRequest request) {
        Cliente cliente = requestToCliente(request);
        Cliente clienteCreado = clienteService.crearCliente(cliente);
        return ResponseEntity.status(HttpStatus.CREATED).body(clienteToResponse(clienteCreado));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> actualizar(@PathVariable Long id, @Valid @RequestBody ClienteRequest request) {
        return clienteService.obtenerClientePorId(id)
                .map(clienteExistente -> {
                    Cliente clienteActualizado = requestToCliente(request);
                    clienteActualizado.setPersonaId(clienteExistente.getPersonaId());
                    clienteService.actualizarCliente(clienteActualizado);
                    return ResponseEntity.noContent().<Void>build();
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ClienteResponse> actualizarEstado(
            @PathVariable Long id,
            @RequestBody Map<String, Object> updates) {
        return clienteService.obtenerClientePorId(id)
                .map(cliente -> {
                    if (updates.containsKey("estado")) {
                        cliente.setEstado((Boolean) updates.get("estado"));
                    }
                    if (updates.containsKey("contrasena")) {
                        cliente.setContrasena((String) updates.get("contrasena"));
                    }
                    clienteService.actualizarCliente(cliente);
                    return ResponseEntity.ok(clienteToResponse(cliente));
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * DELETE /api/clientes/{id} - Eliminar cliente
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        clienteService.eliminarCliente(id);
        return ResponseEntity.noContent().build();
    }

    // Helper methods

    private ClienteResponse clienteToResponse(Cliente cliente) {
        return ClienteResponse.builder()
                .personaId(cliente.getPersonaId())
                .nombre(cliente.getNombre())
                .genero(cliente.getGenero())
                .edad(cliente.getEdad())
                .identificacion(cliente.getIdentificacion())
                .direccion(cliente.getDireccion())
                .telefono(cliente.getTelefono())
                .clienteId(cliente.getClienteId())
                .estado(cliente.getEstado())
                .build();
    }

    private Cliente requestToCliente(ClienteRequest request) {
        Cliente cliente = new Cliente();
        cliente.setNombre(request.getNombre());
        cliente.setGenero(request.getGenero());
        cliente.setEdad(request.getEdad());
        cliente.setIdentificacion(request.getIdentificacion());
        cliente.setDireccion(request.getDireccion());
        cliente.setTelefono(request.getTelefono());
        cliente.setClienteId(request.getClienteId());
        cliente.setContrasena(request.getContrasena());
        cliente.setEstado(request.getEstado());
        return cliente;
    }

}

