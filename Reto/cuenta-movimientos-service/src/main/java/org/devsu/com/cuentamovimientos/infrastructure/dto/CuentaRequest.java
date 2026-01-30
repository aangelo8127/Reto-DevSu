package org.devsu.com.cuentamovimientos.infrastructure.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.DecimalMin;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CuentaRequest {

    @NotBlank(message = "El número de cuenta es requerido")
    private String numeroCuenta;

    @NotBlank(message = "El tipo de cuenta es requerido")
    private String tipoCuenta;

    @NotNull(message = "El saldo inicial es requerido")
    @DecimalMin(value = "0.0", message = "El saldo inicial debe ser mayor o igual a 0")
    private BigDecimal saldoInicial;

    @NotBlank(message = "El clienteId es requerido")
    private String clienteId;

    private Boolean estado = true;

}
