package com.toolrental.usuario.model;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "Direccion")

public class Direccion {
@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idDireccion;

    @NotBlank(message = "La calle es obligatoria")
    @Size(min = 3, max = 100)
    @Column(nullable = false, length = 100)
    private String calle;

    @NotNull(message = "La numeración es obligatoria")
    @Column(nullable = false)
    private Integer numeracion;

    @NotNull(message = "La comuna es obligatoria")
    @ManyToOne
    @JoinColumn(name = "idComuna", nullable = false)
    private Comuna comuna;

    @NotNull(message = "El usuario es obligatorio")
    @ManyToOne
    @JoinColumn(name = "rutUsuario", nullable = false)
    private Usuario111 usuario;
}


