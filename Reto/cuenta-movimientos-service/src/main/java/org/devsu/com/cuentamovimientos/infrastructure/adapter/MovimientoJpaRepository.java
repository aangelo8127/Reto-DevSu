package org.devsu.com.cuentamovimientos.infrastructure.adapter;

import org.devsu.com.cuentamovimientos.domain.entity.Movimiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovimientoJpaRepository extends JpaRepository<Movimiento, Long> {

    List<Movimiento> findByCuentaId(Long cuentaId);

}
