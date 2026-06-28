package com.tool_rental.herramientas.DTO;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ResenaDTO {
    private Integer idResena;
    private Integer puntuacion;
    private String comentario;
    private LocalDate fechaResena;
    private Integer reservaId;
}
