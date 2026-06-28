package com.tool_rental.herramientas.DTO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class MaterialDTO {
    private Integer idMaterialDTO;
    private String nombreMaterialDTO;
    private String descripcionMaterialDTO;
}
