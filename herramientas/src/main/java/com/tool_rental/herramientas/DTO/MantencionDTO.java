package com.tool_rental.herramientas.DTO;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class MantencionDTO {
    private Integer idMantencion;
    private LocalDate fechaUltimaMantencion;
    private Integer vigenciaMeses;
    private String descripcion;
    private String estado;
}
