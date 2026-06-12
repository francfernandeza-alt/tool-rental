package com.tool_rental.herramientas.Model;

import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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
@Table(name = "Herramienta")
public class Herramienta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idHerramienta;

    @NotBlank (message = "El nombre es obligatorio")
    @Size(min = 3, max = 100, message = "El nombre debe tener entre 3 y 100 caracteres")
    @Column(nullable = false, length = 100)
    private String nombreHerramienta;

    @NotBlank (message = "La descripción es obligatoria")
    @Size(min = 20, max = 500, message = "La descripción debe tener entre 20 y 500 caracteres")
    @Column(nullable = false, length = 500)
    private String descripcionHerramienta;

    @NotBlank (message = "El estado es obligatorio")
    @Size(min = 2, max = 100, message = "El estado debe tener entre 2 y 100 caracteres")
    @Column(nullable = false, length = 100)
    private String estadoHerramienta;

    @NotNull
    @Column(nullable = false)
    private Integer cantidadTotal;

    @NotNull
    @Column(nullable = false)
    private Integer cantidadDisponible;

    @NotNull
    @Column(nullable = false)
    private LocalDate fechaActualizacion;

    @OneToMany(mappedBy = "herramienta")
    private List<TiposHerramientas> tiposherramientas;

    @OneToMany(mappedBy = "herramienta")
    private List<Mantenciones> mantenciones;

    @OneToMany(mappedBy = "herramienta")
    private List<Materiales> materiales;

    @ManyToOne
    @JoinColumn(name = "idMarca", nullable = false)
    private Marca marca;
}
