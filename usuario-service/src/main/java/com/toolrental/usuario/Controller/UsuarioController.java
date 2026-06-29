package com.toolrental.usuario.controller;

import java.util.List;

import jakarta.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.toolrental.usuario.dto.UsuarioDTO;
import com.toolrental.usuario.model.Usuario111;
import com.toolrental.usuario.service.UsuarioService;

@RestController
@RequestMapping("/api/v1/usuarios")
public class UsuarioController {

private static final Logger logger = LoggerFactory.getLogger(UsuarioController.class);

private final UsuarioService usuarioService;

public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
}

@GetMapping
public ResponseEntity<List<UsuarioDTO>> obtenerTodos() {

        logger.info("Solicitando listado de usuarios");

        List<UsuarioDTO> usuarios = usuarioService.obtenerTodos();

        if (usuarios.isEmpty()) {
        logger.warn("No existen usuarios registrados");
        return ResponseEntity.noContent().build();
        }

        logger.info("Se encontraron {} usuarios", usuarios.size());

return ResponseEntity.ok(usuarios);
}

@GetMapping("/{rut}")
public ResponseEntity<UsuarioDTO> buscarPorRut(@PathVariable String rut) {

        logger.info("Buscando usuario con RUT {}", rut);

        return ResponseEntity.ok(usuarioService.buscarPorRut(rut));
}

@PostMapping
public ResponseEntity<Usuario111> guardar(@Valid @RequestBody Usuario111 usuario) {

        logger.info("Registrando nuevo usuario");

        return new ResponseEntity<>(usuarioService.guardar(usuario), HttpStatus.CREATED);
}

@PutMapping("/{rut}")
public ResponseEntity<Usuario111> actualizar(
        @PathVariable String rut,
        @RequestBody Usuario111 usuario) {

        logger.info("Actualizando usuario con RUT {}", rut);

        return ResponseEntity.ok(usuarioService.actualizar(rut, usuario));
}

@DeleteMapping("/{rut}")
public ResponseEntity<String> eliminar(@PathVariable String rut) {

        logger.info("Eliminando usuario con RUT {}", rut);

        return ResponseEntity.ok(usuarioService.eliminar(rut));
}
}