package com.toolrental.usuario.dto;
import lombok.Data;

@Data
public class UsuarioDTO {
    private String rutUsuario;
    private String nombreUsuario;
    private String apellidoPaterno;
    private String apellidoMaterno;
    private String tipoUsuario;
    private String emailUsuario;
}
