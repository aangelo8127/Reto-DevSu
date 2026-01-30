package org.devsu.com.cuentamovimientos.infrastructure.adapter;

import lombok.RequiredArgsConstructor;
import org.devsu.com.cuentamovimientos.domain.entity.Cuenta;
import org.devsu.com.cuentamovimientos.domain.port.CuentaRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CuentaRepositoryAdapter implements CuentaRepository {

    private final CuentaJpaRepository cuentaJpaRepository;

    @Override
    public Cuenta save(Cuenta cuenta) {
        return cuentaJpaRepository.save(cuenta);
    }

    @Override
    public Optional<Cuenta> findById(Long id) {
        return cuentaJpaRepository.findById(id);
    }

    @Override
    public Optional<Cuenta> findByNumeroCuenta(String numeroCuenta) {
        return cuentaJpaRepository.findByNumeroCuenta(numeroCuenta);
    }

    @Override
    public List<Cuenta> findByClienteId(String clienteId) {
        return cuentaJpaRepository.findByClienteId(clienteId);
    }

    @Override
    public List<Cuenta> findAll() {
        return cuentaJpaRepository.findAll();
    }

    @Override
    public void update(Cuenta cuenta) {
        cuentaJpaRepository.save(cuenta);
    }

    @Override
    public void deleteById(Long id) {
        cuentaJpaRepository.deleteById(id);
    }

}
