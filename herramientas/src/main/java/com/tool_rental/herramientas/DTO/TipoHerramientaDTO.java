package com.tool_rental.herramientas.DTO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class TipoHerramientaDTO {
    private Integer idTipoHerramientaDTO;
    private String nombreTipoHerramientaDTO;
    private String descripcionTipoHerramientaDTO;
}
