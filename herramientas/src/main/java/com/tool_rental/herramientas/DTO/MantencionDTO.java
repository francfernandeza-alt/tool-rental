package com.tool_rental.herramientas.DTO;

import java.time.LocalDate;

import lombok.Data;

@Data
public class MantencionDTO {
    private Integer idMantencion;
    private LocalDate fechaUltimaMantencion;
    private Integer vigenciaMeses;
    private String descripcion;
    private String estado;
}
