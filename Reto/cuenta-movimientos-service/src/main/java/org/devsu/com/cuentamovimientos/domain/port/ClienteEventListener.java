package org.devsu.com.cuentamovimientos.domain.port;

import org.devsu.com.cuentamovimientos.domain.entity.Cuenta;

public interface ClienteEventListener {

    void onClienteCreated(Cuenta cuenta);

    void onClienteUpdated(Cuenta cuenta);

    void onClienteDeleted(String clienteId);

}
