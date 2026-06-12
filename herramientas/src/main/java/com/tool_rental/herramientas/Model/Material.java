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
@Table(name = "Material")
public class Material {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idMaterial;

    @NotBlank(message = "El nombre del material es obligatorio")
    @Size(min = 3, max = 100, message = "El nombre del material debe tener entre 3 y 100 caracteres")
    @Column(nullable = false, length = 100)
    private String nombreMaterial;

    @NotBlank(message= "La descripcion del material es obligatoria")
    @Size(min = 3, max = 255, message= "La descripción del material debe tener entre 3 y 255 caracteres")
    @Column(nullable = false, length = 255)
    private String descripcionMaterial;

    @OneToMany(mappedBy = "material")
    private List<Materiales> materiales;
}
