package org.devsu.com.clientepersona.application.usecase;

import lombok.RequiredArgsConstructor;
import org.devsu.com.clientepersona.domain.entity.Cliente;
import org.devsu.com.clientepersona.domain.port.ClienteEventPublisher;
import org.devsu.com.clientepersona.domain.port.ClienteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Caso de Uso: ClienteService
 * Implementa la lógica de negocio para operaciones CRUD de Cliente.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class ClienteService {

    private final ClienteRepository clienteRepository;
    private final ClienteEventPublisher clienteEventPublisher;

    /**
     * Crear un nuevo cliente.
     */
    public Cliente crearCliente(Cliente cliente) {
        Cliente clienteGuardado = clienteRepository.save(cliente);
        clienteEventPublisher.publishClienteCreated(clienteGuardado);
        return clienteGuardado;
    }

    /**
     * Obtener cliente por ID.
     */
    public Optional<Cliente> obtenerClientePorId(Long id) {
        return clienteRepository.findById(id);
    }

    /**
     * Obtener cliente por ClienteId.
     */
    public Optional<Cliente> obtenerClientePorClienteId(String clienteId) {
        return clienteRepository.findByClienteId(clienteId);
    }

    /**
     * Obtener todos los clientes.
     */
    public List<Cliente> obtenerTodosLosClientes() {
        return clienteRepository.findAll();
    }

    /**
     * Actualizar cliente existente.
     */
    public void actualizarCliente(Cliente cliente) {
        clienteRepository.update(cliente);
        clienteEventPublisher.publishClienteUpdated(cliente);
    }

    public void eliminarCliente(Long id) {
        Optional<Cliente> cliente = clienteRepository.findById(id);
        if (cliente.isPresent()) {
            clienteRepository.deleteById(id);
            clienteEventPublisher.publishClienteDeleted(cliente.get().getClienteId());
        }
    }

}
