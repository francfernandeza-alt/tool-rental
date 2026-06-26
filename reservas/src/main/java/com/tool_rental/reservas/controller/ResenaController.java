package com.tool_rental.reservas.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tool_rental.reservas.assembler.ResenaModelAssembler;
import com.tool_rental.reservas.dto.ResenaDTO;
import com.tool_rental.reservas.model.Resena;
import com.tool_rental.reservas.service.ResenaService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v1/resenas")
@Tag(name = "ReseÃ±as", description = "Endpoints para gestionar reseÃ±as asociadas a reservas y herramientas")
@Slf4j
public class ResenaController {

    @Autowired
    private ResenaService resenaService;

    @Autowired
    private ResenaModelAssembler assembler;

    @Operation(summary = "Listar reseÃ±as", description = "Obtiene todas las reseÃ±as registradas")
    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<ResenaDTO>>> listarResenas() {
        log.info("Solicitud recibida: listar reseÃ±as");
        List<ResenaDTO> resenas = resenaService.obtenerTodos();

        if (resenas.isEmpty()) {
            log.warn("No existen reseÃ±as registradas");
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        log.info("ReseÃ±as listadas correctamente. Total: {}", resenas.size());
        return new ResponseEntity<>(assembler.toCollectionModel(resenas), HttpStatus.OK);
    }

    @Operation(summary = "Buscar reseÃ±a por ID", description = "Obtiene una reseÃ±a mediante su identificador")
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<ResenaDTO>> buscarResena(@PathVariable Integer id) {
        log.info("Solicitud recibida: buscar reseÃ±a con ID {}", id);
        ResenaDTO resena = resenaService.buscarPorId(id);

        log.info("ReseÃ±a con ID {} encontrada correctamente", id);
        return new ResponseEntity<>(assembler.toModel(resena), HttpStatus.OK);
    }

    @Operation(summary = "Buscar reseÃ±as por usuario", description = "Obtiene las reseÃ±as asociadas a un RUT de usuario")
    @GetMapping("/usuario/{rutUsuario}")
    public ResponseEntity<CollectionModel<EntityModel<ResenaDTO>>> buscarResenasPorUsuario(@PathVariable String rutUsuario) {
        log.info("Solicitud recibida: buscar reseÃ±as del usuario {}", rutUsuario);
        List<ResenaDTO> resenas = resenaService.buscarPorRutUsuario(rutUsuario);

        if (resenas.isEmpty()) {
            log.warn("No existen reseÃ±as asociadas al usuario {}", rutUsuario);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        log.info("ReseÃ±as del usuario {} obtenidas correctamente. Total: {}", rutUsuario, resenas.size());
        return new ResponseEntity<>(assembler.toCollectionModel(resenas), HttpStatus.OK);
    }

    @Operation(summary = "Buscar reseÃ±as por herramienta", description = "Obtiene las reseÃ±as asociadas a una herramienta")
    @GetMapping("/herramienta/{herramientaId}")
    public ResponseEntity<CollectionModel<EntityModel<ResenaDTO>>> buscarResenasPorHerramienta(@PathVariable Integer herramientaId) {
        log.info("Solicitud recibida: buscar reseÃ±as de la herramienta {}", herramientaId);
        List<ResenaDTO> resenas = resenaService.buscarPorHerramientaId(herramientaId);

        if (resenas.isEmpty()) {
            log.warn("No existen reseÃ±as asociadas a la herramienta {}", herramientaId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        log.info("ReseÃ±as de la herramienta {} obtenidas correctamente. Total: {}", herramientaId, resenas.size());
        return new ResponseEntity<>(assembler.toCollectionModel(resenas), HttpStatus.OK);
    }

    @Operation(summary = "Buscar reseÃ±as por reserva", description = "Obtiene las reseÃ±as asociadas a una reserva")
    @GetMapping("/reserva/{reservaId}")
    public ResponseEntity<CollectionModel<EntityModel<ResenaDTO>>> buscarResenasPorReserva(@PathVariable Integer reservaId) {
        log.info("Solicitud recibida: buscar reseÃ±as de la reserva {}", reservaId);
        List<ResenaDTO> resenas = resenaService.buscarPorReservaId(reservaId);

        if (resenas.isEmpty()) {
            log.warn("No existen reseÃ±as asociadas a la reserva {}", reservaId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        log.info("ReseÃ±as de la reserva {} obtenidas correctamente. Total: {}", reservaId, resenas.size());
        return new ResponseEntity<>(assembler.toCollectionModel(resenas), HttpStatus.OK);
    }

    @Operation(summary = "Crear reseÃ±a", description = "Registra una reseÃ±a validando puntuaciÃ³n, reserva y herramienta")
    @PostMapping
    public ResponseEntity<EntityModel<ResenaDTO>> guardarResena(@Valid @RequestBody Resena resena) {
        log.info("Solicitud recibida: crear reseÃ±a para reserva {} y herramienta {}",
                resena.getReservaId(),
                resena.getHerramientaId());
        ResenaDTO nuevaResena = resenaService.guardar(resena);

        log.info("ReseÃ±a creada correctamente con ID {}", nuevaResena.getIdResena());
        return new ResponseEntity<>(assembler.toModel(nuevaResena), HttpStatus.CREATED);
    }

    @Operation(summary = "Actualizar reseÃ±a", description = "Actualiza una reseÃ±a existente")
    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<ResenaDTO>> actualizarResena(@PathVariable Integer id, @RequestBody Resena resena) {
        log.info("Solicitud recibida: actualizar reseÃ±a con ID {}", id);
        ResenaDTO resenaActualizada = resenaService.actualizar(id, resena);

        log.info("ReseÃ±a con ID {} actualizada correctamente", id);
        return new ResponseEntity<>(assembler.toModel(resenaActualizada), HttpStatus.OK);
    }

    @Operation(summary = "Eliminar reseÃ±a", description = "Elimina una reseÃ±a mediante su identificador")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarResena(@PathVariable Integer id) {
        log.info("Solicitud recibida: eliminar reseÃ±a con ID {}", id);
        String mensaje = resenaService.eliminar(id);

        log.info("ReseÃ±a con ID {} eliminada correctamente", id);
        return new ResponseEntity<>(mensaje, HttpStatus.OK);
    }
}


