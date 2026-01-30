package org.devsu.com.cuentamovimientos.domain.port;

import org.devsu.com.cuentamovimientos.domain.entity.Cuenta;

import java.util.List;
import java.util.Optional;

public interface CuentaRepository {

    Cuenta save(Cuenta cuenta);

    Optional<Cuenta> findById(Long id);

    Optional<Cuenta> findByNumeroCuenta(String numeroCuenta);

    List<Cuenta> findByClienteId(String clienteId);

    List<Cuenta> findAll();

    void update(Cuenta cuenta);

    void deleteById(Long id);

}
