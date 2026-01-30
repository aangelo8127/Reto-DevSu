package org.devsu.com.clientepersona.infrastructure.adapter;

import lombok.RequiredArgsConstructor;
import org.devsu.com.clientepersona.domain.entity.Cliente;
import org.devsu.com.clientepersona.domain.port.ClienteRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ClienteRepositoryAdapter implements ClienteRepository {

    private final ClienteJpaRepository clienteJpaRepository;

    @Override
    public Cliente save(Cliente cliente) {
        return clienteJpaRepository.save(cliente);
    }

    @Override
    public Optional<Cliente> findById(Long id) {
        return clienteJpaRepository.findById(id);
    }

    @Override
    public Optional<Cliente> findByClienteId(String clienteId) {
        return clienteJpaRepository.findByClienteId(clienteId);
    }

    @Override
    public List<Cliente> findAll() {
        return clienteJpaRepository.findAll();
    }

    @Override
    public void update(Cliente cliente) {
        clienteJpaRepository.save(cliente);
    }

    @Override
    public void deleteById(Long id) {
        clienteJpaRepository.deleteById(id);
    }

}
