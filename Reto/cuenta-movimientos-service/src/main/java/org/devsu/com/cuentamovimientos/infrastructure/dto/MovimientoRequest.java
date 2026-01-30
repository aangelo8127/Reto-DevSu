package org.devsu.com.cuentamovimientos.infrastructure.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.DecimalMin;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MovimientoRequest {

    @NotNull(message = "El tipo de movimiento es requerido")
    private String tipoMovimiento;

    @NotNull(message = "El valor es requerido")
    @DecimalMin(value = "0.01", message = "El valor debe ser mayor a 0")
    private BigDecimal valor;

}
