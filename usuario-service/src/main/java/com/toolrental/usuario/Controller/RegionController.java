package com.toolrental.usuario.controller;

import java.util.List;

import jakarta.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.toolrental.usuario.dto.RegionDTO;
import com.toolrental.usuario.model.Region;
import com.toolrental.usuario.service.RegionService;

@RestController
@RequestMapping("/api/v1/region")
public class RegionController {

private static final Logger logger = LoggerFactory.getLogger(RegionController.class);

@Autowired
private RegionService regionService;

@GetMapping
public ResponseEntity<List<RegionDTO>> obtenerTodas() {

        logger.info("Solicitando listado de regiones");

        List<RegionDTO> regiones = regionService.obtenerTodas();

        if (regiones.isEmpty()) {
        logger.warn("No existen regiones registradas");
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        logger.info("Se encontraron {} regiones", regiones.size());

        return new ResponseEntity<>(regiones, HttpStatus.OK);
}

@GetMapping("/{id}")
public ResponseEntity<RegionDTO> buscarPorId(@PathVariable Integer id) {

        logger.info("Buscando región con id {}", id);

        return new ResponseEntity<>(regionService.buscarPorId(id), HttpStatus.OK);
}

@PostMapping
public ResponseEntity<Region> guardar(@Valid @RequestBody Region region) {

        logger.info("Registrando nueva región");

        return new ResponseEntity<>(regionService.guardar(region), HttpStatus.CREATED);
}

@PutMapping("/{id}")
public ResponseEntity<Region> actualizar(
        @PathVariable Integer id,
        @Valid @RequestBody Region region) {

        logger.info("Actualizando región con id {}", id);

        return new ResponseEntity<>(regionService.actualizar(id, region), HttpStatus.OK);
}

@DeleteMapping("/{id}")
public ResponseEntity<String> eliminar(@PathVariable Integer id) {

        logger.info("Eliminando región con id {}", id);

        return new ResponseEntity<>(regionService.eliminar(id), HttpStatus.OK);
}
}