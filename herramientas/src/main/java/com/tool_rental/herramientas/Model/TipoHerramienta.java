package com.tool_rental.herramientas.Model;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
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
@Table(name = "TipoHerramienta")
public class TipoHerramienta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idTipoHerramienta;

    @NotBlank(message = "El nombre del tipo de herramienta es obligatorio")
    @Size(min = 3, max = 100, message = "El nombre del tipo de herramienta debe tener entre 3 y 100 caracteres")
    @Column(nullable = false, length = 100)
    private String nombreTipoHerramienta;

    @NotBlank(message = "La descripción del tipo de herramienta es obligatoria")
    @Size(min = 3, max = 255, message = "La descripción del tipo de herramienta debe tener entre 3 y 255 caracteres")
    @Column(nullable = false, length = 255)
    private String descripcionTipoHerramienta;

    @OneToMany(mappedBy = "tipoHerramienta")
    private List<TiposHerramientas> tiposherramientas;
}
