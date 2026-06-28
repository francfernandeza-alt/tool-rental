package com.tool_rental.herramientas.DTO;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class HerramientaDTO {
    private Integer idHerramientaDTO;
    private String nombreHerramientaDTO;
    private String estadoHerramientaDTO;
    private Integer cantidadDisponibleDTO;
    private String nombreMarcaDTO;
    private Double promedioEvaluacion;
    private Integer totalResenas;
    private List <ResenaDTO> resenas;
}
