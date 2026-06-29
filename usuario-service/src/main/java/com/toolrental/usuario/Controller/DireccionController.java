package com.toolrental.usuario.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import com.toolrental.usuario.dto.DireccionDTO;
import com.toolrental.usuario.model.Direccion;
import com.toolrental.usuario.service.DireccionService;

@RestController
@RequestMapping("/api/v1/direccion")
public class DireccionController {

    private static final Logger logger = LoggerFactory.getLogger(DireccionController.class);

    @Autowired
    private DireccionService direccionService;

    @GetMapping
    public ResponseEntity<List<DireccionDTO>> obtenerTodas() {

        logger.info("Solicitando listado de direcciones");

        List<DireccionDTO> lista = direccionService.obtenerTodas();

        if (lista.isEmpty()) {
            logger.warn("No existen direcciones registradas");
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        logger.info("Se encontraron {} direcciones", lista.size());

        return new ResponseEntity<>(lista, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DireccionDTO> buscarPorId(@PathVariable Integer id) {

        logger.info("Buscando dirección con id {}", id);

        return new ResponseEntity<>(direccionService.buscarPorId(id), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Direccion> guardar(@Valid @RequestBody Direccion direccion) {

        logger.info("Registrando nueva dirección");

        return new ResponseEntity<>(direccionService.guardar(direccion), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Direccion> actualizar(
            @PathVariable Integer id,
            @RequestBody Direccion direccion) {

        logger.info("Actualizando dirección con id {}", id);

        return new ResponseEntity<>(direccionService.actualizar(id, direccion), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminar(@PathVariable Integer id) {

        logger.info("Eliminando dirección con id {}", id);

        return new ResponseEntity<>(direccionService.eliminar(id), HttpStatus.OK);
    }
}