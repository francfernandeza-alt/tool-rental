package com.tool_rental.herramientas.DTO;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class ErrorDTO {
    private String mensaje;
    private String detalle;
    private Integer codigoEstado;
    private LocalDateTime fecha;
}
