package org.devsu.com.clientepersona.domain.port;

import org.devsu.com.clientepersona.domain.entity.Cliente;

import java.util.List;
import java.util.Optional;

public interface ClienteRepository {

    Cliente save(Cliente cliente);

    Optional<Cliente> findById(Long id);

    Optional<Cliente> findByClienteId(String clienteId);

    List<Cliente> findAll();

    void update(Cliente cliente);

    void deleteById(Long id);

}
