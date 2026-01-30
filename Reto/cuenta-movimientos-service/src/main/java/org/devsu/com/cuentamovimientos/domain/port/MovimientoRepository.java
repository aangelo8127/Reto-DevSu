package org.devsu.com.cuentamovimientos.domain.port;

import org.devsu.com.cuentamovimientos.domain.entity.Movimiento;

import java.util.List;
import java.util.Optional;

public interface MovimientoRepository {

    Movimiento save(Movimiento movimiento);

    Optional<Movimiento> findById(Long id);

    List<Movimiento> findByCuentaId(Long cuentaId);

    List<Movimiento> findAll();

    void deleteById(Long id);

}
