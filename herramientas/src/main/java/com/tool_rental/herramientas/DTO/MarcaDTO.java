package com.tool_rental.herramientas.DTO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class MarcaDTO {
    private Integer idMarcaDTO;
    private String nombreMarcaDTO;
}
