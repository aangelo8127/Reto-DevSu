package org.devsu.com.cuentamovimientos.application.usecase;

import lombok.RequiredArgsConstructor;
import org.devsu.com.cuentamovimientos.domain.entity.Cuenta;
import org.devsu.com.cuentamovimientos.domain.entity.Movimiento;
import org.devsu.com.cuentamovimientos.domain.port.CuentaRepository;
import org.devsu.com.cuentamovimientos.domain.port.MovimientoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class MovimientoService {

    private final MovimientoRepository movimientoRepository;
    private final CuentaRepository cuentaRepository;

    public Movimiento registrarDebito(Long cuentaId, BigDecimal valor) throws IllegalArgumentException {
        Optional<Cuenta> cuentaOpt = cuentaRepository.findById(cuentaId);
        if (cuentaOpt.isEmpty()) {
            throw new IllegalArgumentException("Cuenta no encontrada");
        }

        Cuenta cuenta = cuentaOpt.get();

        // Validar saldo disponible
        if (cuenta.getSaldoDisponible().compareTo(valor) < 0) {
            throw new IllegalArgumentException("Saldo insuficiente para realizar el débito");
        }

        // Crear movimiento
        Movimiento movimiento = new Movimiento();
        movimiento.setFecha(LocalDateTime.now());
        movimiento.setTipoMovimiento("Debito");
        movimiento.setValor(valor);
        movimiento.setCuenta(cuenta);

        // Actualizar saldo
        BigDecimal nuevoSaldo = cuenta.getSaldoDisponible().subtract(valor);
        movimiento.setSaldoDisponible(nuevoSaldo);
        cuenta.setSaldoDisponible(nuevoSaldo);

        // Guardar
        Movimiento movimientoGuardado = movimientoRepository.save(movimiento);
        cuentaRepository.update(cuenta);

        return movimientoGuardado;
    }

    public Movimiento registrarCredito(Long cuentaId, BigDecimal valor) throws IllegalArgumentException {
        Optional<Cuenta> cuentaOpt = cuentaRepository.findById(cuentaId);
        if (cuentaOpt.isEmpty()) {
            throw new IllegalArgumentException("Cuenta no encontrada");
        }

        Cuenta cuenta = cuentaOpt.get();

        Movimiento movimiento = new Movimiento();
        movimiento.setFecha(LocalDateTime.now());
        movimiento.setTipoMovimiento("Credito");
        movimiento.setValor(valor);
        movimiento.setCuenta(cuenta);

        BigDecimal nuevoSaldo = cuenta.getSaldoDisponible().add(valor);
        movimiento.setSaldoDisponible(nuevoSaldo);
        cuenta.setSaldoDisponible(nuevoSaldo);

        Movimiento movimientoGuardado = movimientoRepository.save(movimiento);
        cuentaRepository.update(cuenta);

        return movimientoGuardado;
    }

    public Optional<Movimiento> obtenerMovimientoPorId(Long id) {
        return movimientoRepository.findById(id);
    }

    public List<Movimiento> obtenerMovimientosPorCuenta(Long cuentaId) {
        return movimientoRepository.findByCuentaId(cuentaId);
    }

    public List<Movimiento> obtenerTodosLosMovimientos() {
        return movimientoRepository.findAll();
    }

}
