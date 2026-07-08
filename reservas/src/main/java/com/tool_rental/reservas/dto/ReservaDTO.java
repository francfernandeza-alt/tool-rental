package com.tool_rental.reservas.dto;

import java.time.LocalDate;

import lombok.Data;

@Data
public class ReservaDTO {

    private Integer idReserva;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private String estadoReserva;
    private String rutUsuario;

    private Integer tipoReservaId;
    private String nombreTipoReserva;

    private Integer metodoPagoId;
    private String nombreMetodoPago;

    private Boolean activo;
}


