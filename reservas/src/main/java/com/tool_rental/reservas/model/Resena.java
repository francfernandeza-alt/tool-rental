package com.tool_rental.reservas.model;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
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
@Table(name = "resenas")
public class Resena {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idResena;

    @NotNull(message = "La puntuacion es obligatoria")
    @Min(value = 1, message = "La puntuacion minima es 1")
    @Max(value = 5, message = "La puntuacion maxima es 5")
    @Column(nullable = false)
    private Integer puntuacion;

    @NotBlank(message = "El comentario de la resena es obligatorio")
    @Size(min = 3, max = 255, message = "El comentario debe tener entre 3 y 255 caracteres")
    @Column(nullable = false, length = 255)
    private String comentario;

    @Column(nullable = false)
    private LocalDate fechaResena;

    @NotBlank(message = "El RUT del usuario es obligatorio")
    @Size(min = 8, max = 12, message = "El RUT del usuario debe tener entre 8 y 12 caracteres")
    @Column(nullable = false, length = 12)
    private String rutUsuario;

    @NotNull(message = "El ID de la herramienta es obligatorio")
    @Column(nullable = false)
    private Integer herramientaId;

    @NotNull(message = "El ID de la reserva es obligatorio")
    @Column(nullable = false)
    private Integer reservaId;

    @Column(nullable = false)
    private Boolean activo = true;
}
