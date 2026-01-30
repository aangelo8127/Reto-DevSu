package org.devsu.com.clientepersona.infrastructure.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClienteResponse {

    private Long personaId;

    private String nombre;

    private String genero;

    private Integer edad;

    private String identificacion;

    private String direccion;

    private String telefono;

    private String clienteId;

    private Boolean estado;

}
