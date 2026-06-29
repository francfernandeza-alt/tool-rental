package com.toolrental.usuario.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.toolrental.usuario.dto.ComunaDTO;
import com.toolrental.usuario.model.Comuna;
import com.toolrental.usuario.service.ComunaService;

@RestController
@RequestMapping("/api/v1/comuna")
public class ComunaController {

    private static final Logger logger = LoggerFactory.getLogger(ComunaController.class);

    @Autowired
    private ComunaService comunaService;

    @GetMapping
    public ResponseEntity<List<ComunaDTO>> obtenerTodas() {

        logger.info("Solicitando listado de comunas");

        List<ComunaDTO> comunas = comunaService.obtenerTodas();

        if (comunas.isEmpty()) {
            logger.warn("No existen comunas registradas");
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        logger.info("Se encontraron {} comunas", comunas.size());

        return new ResponseEntity<>(comunas, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ComunaDTO> buscarPorId(@PathVariable Integer id) {

        logger.info("Buscando comuna con id {}", id);

        return new ResponseEntity<>(comunaService.buscarPorId(id), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Comuna> guardar(@RequestBody Comuna comuna) {

        logger.info("Registrando nueva comuna");

        return new ResponseEntity<>(comunaService.guardarComuna(comuna), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Comuna> actualizar(
            @PathVariable Integer id,
            @RequestBody Comuna comuna) {

        logger.info("Actualizando comuna con id {}", id);

        return new ResponseEntity<>(comunaService.actualizarComuna(id, comuna), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminar(@PathVariable Integer id) {

        logger.info("Eliminando comuna con id {}", id);

        return new ResponseEntity<>(comunaService.eliminar(id), HttpStatus.OK);
    }
}