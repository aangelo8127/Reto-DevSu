package org.devsu.com.cuentamovimientos.application.usecase;

import lombok.RequiredArgsConstructor;
import org.devsu.com.cuentamovimientos.domain.entity.Cuenta;
import org.devsu.com.cuentamovimientos.domain.port.CuentaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class CuentaService {

    private final CuentaRepository cuentaRepository;

    public Cuenta crearCuenta(Cuenta cuenta) {
        return cuentaRepository.save(cuenta);
    }

    public Optional<Cuenta> obtenerCuentaPorId(Long id) {
        return cuentaRepository.findById(id);
    }

    public Optional<Cuenta> obtenerCuentaPorNumero(String numeroCuenta) {
        return cuentaRepository.findByNumeroCuenta(numeroCuenta);
    }

    public List<Cuenta> obtenerCuentasPorCliente(String clienteId) {
        return cuentaRepository.findByClienteId(clienteId);
    }

    public List<Cuenta> obtenerTodasLasCuentas() {
        return cuentaRepository.findAll();
    }

    public void actualizarCuenta(Cuenta cuenta) {
        cuentaRepository.update(cuenta);
    }

    public void eliminarCuenta(Long id) {
        cuentaRepository.deleteById(id);
    }

}
