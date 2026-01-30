package org.devsu.com.clientepersona.domain.port;

import org.devsu.com.clientepersona.domain.entity.Cliente;

public interface ClienteEventPublisher {

    void publishClienteCreated(Cliente cliente);

    void publishClienteUpdated(Cliente cliente);

    void publishClienteDeleted(String clienteId);

}
