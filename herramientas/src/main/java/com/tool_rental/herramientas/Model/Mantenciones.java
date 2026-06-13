package com.tool_rental.herramientas.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Mantenciones")
public class Mantenciones {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idMantenciones;

    @NotNull(message = "La herramienta es obligatoria")
    @ManyToOne
    @JoinColumn(name = "idHerramienta", nullable = false)
    private Herramienta herramienta;

    @NotNull(message = "La mantención es obligatoria")
    @ManyToOne
    @JoinColumn(name = "idMantencion", nullable = false)
    private Mantencion mantencion;
}
