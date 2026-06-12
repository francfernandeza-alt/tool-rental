package com.tool_rental.herramientas.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Materiales")
public class Materiales {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idMateriales;

    @ManyToOne
    @JoinColumn(name = "idHerramienta", nullable = false)
    private Herramienta herramienta;

    @ManyToOne
    @JoinColumn(name = "idMaterial", nullable = false)
    private Material material;
}
