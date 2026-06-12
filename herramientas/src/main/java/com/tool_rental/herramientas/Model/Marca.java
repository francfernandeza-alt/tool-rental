package com.tool_rental.herramientas.Model;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Marca")
public class Marca {
    @Id
    @NotNull(message= "El id de la marca es obligatorio")
    @Column(nullable = false)
    private Integer idMarca;

    @NotBlank(message = "El nombre de la marca es obligatorio")
    @Size(min = 3, max = 100, message = "El nombre de la marca debe tener entre 3 y 100 caracteres")
    @Column(nullable = false, length = 100)
    private String nombreMarca;

    @Column(nullable = false, length = 1000)
    private String descripcionMarca;

    @OneToMany(mappedBy = "marca")
    private List<Herramienta> herramientas;
}
