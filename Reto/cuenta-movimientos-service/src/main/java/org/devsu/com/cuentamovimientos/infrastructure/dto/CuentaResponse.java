package org.devsu.com.cuentamovimientos.infrastructure.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CuentaResponse {

    private Long cuentaId;

    private String numeroCuenta;

    private String tipoCuenta;

    private BigDecimal saldoInicial;

    private BigDecimal saldoDisponible;

    private Boolean estado;

    private String clienteId;

}
