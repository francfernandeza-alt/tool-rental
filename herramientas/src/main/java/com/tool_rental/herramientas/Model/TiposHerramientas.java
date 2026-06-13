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
@Table(name = "TiposHerramientas")
public class TiposHerramientas {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idTiposHerramientas;

    @NotNull(message = "La herramienta es obligatoria")
    @ManyToOne
    @JoinColumn(name = "idHerramienta", nullable = false)
    private Herramienta herramienta;

    @NotNull(message = "El tipo de herramienta es obligatorio")
    @ManyToOne
    @JoinColumn(name = "idTipoHerramienta", nullable = false)
    private TipoHerramienta tipoHerramienta;
}
