package org.devsu.com.cuentamovimientos.infrastructure.adapter;

import lombok.RequiredArgsConstructor;
import org.devsu.com.cuentamovimientos.domain.entity.Movimiento;
import org.devsu.com.cuentamovimientos.domain.port.MovimientoRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class MovimientoRepositoryAdapter implements MovimientoRepository {

    private final MovimientoJpaRepository movimientoJpaRepository;

    @Override
    public Movimiento save(Movimiento movimiento) {
        return movimientoJpaRepository.save(movimiento);
    }

    @Override
    public Optional<Movimiento> findById(Long id) {
        return movimientoJpaRepository.findById(id);
    }

    @Override
    public List<Movimiento> findByCuentaId(Long cuentaId) {
        return movimientoJpaRepository.findByCuentaId(cuentaId);
    }

    @Override
    public List<Movimiento> findAll() {
        return movimientoJpaRepository.findAll();
    }

    @Override
    public void deleteById(Long id) {
        movimientoJpaRepository.deleteById(id);
    }

}
