package com.toolrental.usuario.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.toolrental.usuario.dto.UsuarioDTO;
import com.toolrental.usuario.model.Usuario111;
import com.toolrental.usuario.repository.UsuarioRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class UsuarioService {

    private static final Logger logger = LoggerFactory.getLogger(UsuarioService.class);

    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public List<UsuarioDTO> obtenerTodos() {

        logger.info("Obteniendo todos los usuarios");

        return usuarioRepository.findAll()
                .stream()
                .map(this::convertirADTO)
                .toList();
    }

    public UsuarioDTO buscarPorRut(String rut) {

        logger.info("Buscando usuario con rut {}", rut);

        Usuario111 usuario = usuarioRepository.findById(rut)
                .orElseThrow(() -> {
                    logger.error("Usuario con rut {} no encontrado", rut);
                    return new RuntimeException("Usuario no encontrado");
                });

        logger.info("Usuario encontrado correctamente");

        return convertirADTO(usuario);
    }

    public Usuario111 guardar(Usuario111 usuario) {

        logger.info("Guardando nuevo usuario");

        return usuarioRepository.save(usuario);
    }

    public Usuario111 actualizar(String rut, Usuario111 usuario) {

        logger.info("Actualizando usuario con rut {}", rut);

        Usuario111 existente = usuarioRepository.findById(rut)
                .orElseThrow(() -> {
                    logger.error("Usuario con rut {} no encontrado", rut);
                    return new RuntimeException("Usuario no encontrado");
                });

        if (usuario.getNombreUsuario() != null) {
            existente.setNombreUsuario(usuario.getNombreUsuario());
        }

        if (usuario.getApellidoPaterno() != null) {
            existente.setApellidoPaterno(usuario.getApellidoPaterno());
        }

        if (usuario.getApellidoMaterno() != null) {
            existente.setApellidoMaterno(usuario.getApellidoMaterno());
        }

        if (usuario.getTipoUsuario() != null) {
            existente.setTipoUsuario(usuario.getTipoUsuario());
        }

        if (usuario.getEmailUsuario() != null) {
            existente.setEmailUsuario(usuario.getEmailUsuario());
        }

        if (usuario.getContrasenaUsuario() != null) {
            existente.setContrasenaUsuario(usuario.getContrasenaUsuario());
        }

        logger.info("Usuario actualizado correctamente");

        return usuarioRepository.save(existente);
    }

    public String eliminar(String rut) {

        logger.info("Eliminando usuario con rut {}", rut);

        Usuario111 usuario = usuarioRepository.findById(rut)
                .orElseThrow(() -> {
                    logger.error("Usuario con rut {} no encontrado", rut);
                    return new RuntimeException("Usuario no encontrado");
                });

        usuarioRepository.delete(usuario);

        logger.info("Usuario eliminado correctamente");

        return "Usuario eliminado correctamente";
    }

    private UsuarioDTO convertirADTO(Usuario111 usuario) {

        UsuarioDTO dto = new UsuarioDTO();

        dto.setRutUsuario(usuario.getRutUsuario());
        dto.setNombreUsuario(usuario.getNombreUsuario());
        dto.setApellidoPaterno(usuario.getApellidoPaterno());
        dto.setApellidoMaterno(usuario.getApellidoMaterno());
        dto.setTipoUsuario(usuario.getTipoUsuario());
        dto.setEmailUsuario(usuario.getEmailUsuario());

        return dto;
    }
}