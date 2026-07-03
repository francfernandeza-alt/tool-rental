package com.toolrental.usuario.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.Id;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Region")

public class Region {
@Id
    private Integer numeroRegion;

    @NotBlank(message = "El nombre de la región es obligatorio")
    @Column(nullable = false, length = 100)
    private String nombreRegion;
}
