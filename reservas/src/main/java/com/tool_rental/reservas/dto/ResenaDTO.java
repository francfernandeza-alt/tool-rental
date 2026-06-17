package com.tool_rental.reservas.dto;

import java.time.LocalDate;

import lombok.Data;

@Data
public class ResenaDTO {

    private Integer idResena;
    private Integer puntuacion;
    private String comentario;
    private LocalDate fechaResena;
    private String rutUsuario;
    private Integer herramientaId;
    private Integer reservaId;
}
