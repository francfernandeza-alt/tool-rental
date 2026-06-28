package com.tool_rental.herramientas.DTO;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ErrorDTO {
    private String mensaje;
    private String detalle;
    private Integer codigoEstado;
    private LocalDateTime fecha;
}
