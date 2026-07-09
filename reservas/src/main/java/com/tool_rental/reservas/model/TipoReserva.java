package com.tool_rental.reservas.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tipos_reserva")
public class TipoReserva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idTipoReserva;

    @NotBlank(message = "El nombre del tipo de reserva es obligatorio")
    @Size(min = 3, max = 100, message = "El nombre del tipo de reserva debe tener entre 3 y 100 caracteres")
    @Column(nullable = false, length = 100)
    private String nombreTipoReserva;

    @Column(nullable = false)
    private Boolean activo = true;
}
