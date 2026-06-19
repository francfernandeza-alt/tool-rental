package com.toolrental.usuario.Model;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Comuna")
public class Comuna {
@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idComuna;

    @NotBlank(message = "El nombre de la comuna es obligatorio")
    @Size(min = 3, max = 100,
        message = "El nombre de la comuna debe tener entre 3 y 100 caracteres")
    @Column(nullable = false, length = 100)
    private String nombreComuna;

    @NotNull(message = "La región es obligatoria")
    @ManyToOne
    @JoinColumn(name = "numeroRegion", nullable = false)
    private Region region;
}
