package com.tool_rental.reservas.model;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "reservas")
public class Reserva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idReserva;

    @NotNull(message = "La fecha de inicio es obligatoria")
    @Column(nullable = false)
    private LocalDate fechaInicio;

    @NotNull(message = "La fecha de fin es obligatoria")
    @Column(nullable = false)
    private LocalDate fechaFin;

    @NotBlank(message = "El estado de la reserva es obligatorio")
    @Size(min = 3, max = 50, message = "El estado de la reserva debe tener entre 3 y 50 caracteres")
    @Column(nullable = false, length = 50)
    private String estadoReserva;

    @NotBlank(message = "El RUT del usuario es obligatorio")
    @Size(min = 8, max = 12, message = "El RUT del usuario debe tener entre 8 y 12 caracteres")
    @Column(nullable = false, length = 12)
    private String rutUsuario;

    @NotNull(message = "El tipo de reserva es obligatorio")
    @Column(nullable = false)
    private Integer tipoReservaId;

    @NotNull(message = "El metodo de pago es obligatorio")
    @Column(nullable = false)
    private Integer metodoPagoId;

    @Column(nullable = false)
    private Boolean activo = true;
}
