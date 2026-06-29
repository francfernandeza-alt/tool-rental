package com.toolrental.usuario.model;
import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Usuario")

public class Usuario111 {
@Id
@Column(length = 12)
private String rutUsuario;

@NotBlank(message = "El nombre es obligatorio")
@Size(min = 3, max = 100, message = "El nombre debe tener entre 3 y 100 caracteres")
@Column(nullable = false, length = 100)
private String nombreUsuario;

@NotBlank(message = "El apellido paterno es obligatorio")
@Size(min = 3, max = 50,
message = "El apellido paterno debe tener entre 3 y 50 caracteres")
@Column(nullable = false, length = 50)
private String apellidoPaterno;

@NotBlank(message = "El apellido materno es obligatorio")
@Size(min = 3, max = 50,
message = "El apellido materno debe tener entre 3 y 50 caracteres")
@Column(nullable = false, length = 50)
private String apellidoMaterno;

@NotBlank(message = "El tipo de usuario es obligatorio")
@Size(min = 5, max = 20,
message = "El tipo de usuario debe tener entre 5 y 20 caracteres")
@Column(nullable = false, length = 20)
private String tipoUsuario;

@NotBlank(message = "El correo es obligatorio")
@Email(message = "Correo inválido")
@Column(nullable = false, unique = true, length = 100)
private String emailUsuario;

@NotBlank(message = "La contraseña es obligatoria")
@Size(min = 8, max = 20,
        message = "La contraseña debe tener entre 8 y 20 caracteres")
@Column(nullable = false, length = 20)
private String contrasenaUsuario;

}


