package com.tool_rental.herramientas.DTO;

import java.time.LocalDate;

import lombok.Data;

@Data
public class ResenaDTO {
    private Integer idResena;
    private Integer puntuacion;
    private String comentario;
    private LocalDate fechaResena;
    private Integer reservaId;
}
