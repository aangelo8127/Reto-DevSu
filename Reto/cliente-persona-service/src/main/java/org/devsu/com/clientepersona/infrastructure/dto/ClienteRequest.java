package org.devsu.com.clientepersona.infrastructure.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Min;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClienteRequest {

    @NotBlank(message = "El nombre es requerido")
    private String nombre;

    @NotBlank(message = "El género es requerido")
    private String genero;

    @NotNull(message = "La edad es requerida")
    @Min(value = 1, message = "La edad debe ser mayor a 0")
    private Integer edad;

    @NotBlank(message = "La identificación es requerida")
    private String identificacion;

    @NotBlank(message = "La dirección es requerida")
    private String direccion;

    @NotBlank(message = "El teléfono es requerido")
    private String telefono;

    @NotBlank(message = "El clienteId es requerido")
    private String clienteId;

    @NotBlank(message = "La contraseña es requerida")
    private String contrasena;

    private Boolean estado = true;

}
