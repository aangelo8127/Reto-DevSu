package org.devsu.com.cuentamovimientos.infrastructure.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MovimientoResponse {

    private Long movimientoId;

    private LocalDateTime fecha;

    private String tipoMovimiento;

    private BigDecimal valor;

    private BigDecimal saldoDisponible;

    private Long cuentaId;

}
