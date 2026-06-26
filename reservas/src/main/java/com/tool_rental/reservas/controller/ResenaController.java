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
@Tag(name = "Resenas", description = "Endpoints para gestionar resenas asociadas a reservas y herramientas")
@Slf4j
public class ResenaController {

    @Autowired
    private ResenaService resenaService;

    @Autowired
    private ResenaModelAssembler assembler;

    @Operation(summary = "Listar resenas", description = "Obtiene todas las resenas registradas")
    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<ResenaDTO>>> listarResenas() {
        log.info("Solicitud recibida: listar resenas");
        List<ResenaDTO> resenas = resenaService.obtenerTodos();

        if (resenas.isEmpty()) {
            log.warn("No existen resenas registradas");
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        log.info("Resenas listadas correctamente. Total: {}", resenas.size());
        return new ResponseEntity<>(assembler.toCollectionModel(resenas), HttpStatus.OK);
    }

    @Operation(summary = "Buscar resena por ID", description = "Obtiene una resena mediante su identificador")
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<ResenaDTO>> buscarResena(@PathVariable Integer id) {
        log.info("Solicitud recibida: buscar resena con ID {}", id);
        ResenaDTO resena = resenaService.buscarPorId(id);

        log.info("Resena con ID {} encontrada correctamente", id);
        return new ResponseEntity<>(assembler.toModel(resena), HttpStatus.OK);
    }

    @Operation(summary = "Buscar resenas por usuario", description = "Obtiene las resenas asociadas a un RUT de usuario")
    @GetMapping("/usuario/{rutUsuario}")
    public ResponseEntity<CollectionModel<EntityModel<ResenaDTO>>> buscarResenasPorUsuario(@PathVariable String rutUsuario) {
        log.info("Solicitud recibida: buscar resenas del usuario {}", rutUsuario);
        List<ResenaDTO> resenas = resenaService.buscarPorRutUsuario(rutUsuario);

        if (resenas.isEmpty()) {
            log.warn("No existen resenas asociadas al usuario {}", rutUsuario);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        log.info("Resenas del usuario {} obtenidas correctamente. Total: {}", rutUsuario, resenas.size());
        return new ResponseEntity<>(assembler.toCollectionModel(resenas), HttpStatus.OK);
    }

    @Operation(summary = "Buscar resenas por herramienta", description = "Obtiene las resenas asociadas a una herramienta")
    @GetMapping("/herramienta/{herramientaId}")
    public ResponseEntity<CollectionModel<EntityModel<ResenaDTO>>> buscarResenasPorHerramienta(@PathVariable Integer herramientaId) {
        log.info("Solicitud recibida: buscar resenas de la herramienta {}", herramientaId);
        List<ResenaDTO> resenas = resenaService.buscarPorHerramientaId(herramientaId);

        if (resenas.isEmpty()) {
            log.warn("No existen resenas asociadas a la herramienta {}", herramientaId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        log.info("Resenas de la herramienta {} obtenidas correctamente. Total: {}", herramientaId, resenas.size());
        return new ResponseEntity<>(assembler.toCollectionModel(resenas), HttpStatus.OK);
    }

    @Operation(summary = "Buscar resenas por reserva", description = "Obtiene las resenas asociadas a una reserva")
    @GetMapping("/reserva/{reservaId}")
    public ResponseEntity<CollectionModel<EntityModel<ResenaDTO>>> buscarResenasPorReserva(@PathVariable Integer reservaId) {
        log.info("Solicitud recibida: buscar resenas de la reserva {}", reservaId);
        List<ResenaDTO> resenas = resenaService.buscarPorReservaId(reservaId);

        if (resenas.isEmpty()) {
            log.warn("No existen resenas asociadas a la reserva {}", reservaId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        log.info("Resenas de la reserva {} obtenidas correctamente. Total: {}", reservaId, resenas.size());
        return new ResponseEntity<>(assembler.toCollectionModel(resenas), HttpStatus.OK);
    }

    @Operation(summary = "Crear resena", description = "Registra una resena validando puntuacion, reserva y herramienta")
    @PostMapping
    public ResponseEntity<EntityModel<ResenaDTO>> guardarResena(@Valid @RequestBody Resena resena) {
        log.info("Solicitud recibida: crear resena para reserva {} y herramienta {}",
                resena.getReservaId(),
                resena.getHerramientaId());
        ResenaDTO nuevaResena = resenaService.guardar(resena);

        log.info("Resena creada correctamente con ID {}", nuevaResena.getIdResena());
        return new ResponseEntity<>(assembler.toModel(nuevaResena), HttpStatus.CREATED);
    }

    @Operation(summary = "Actualizar resena", description = "Actualiza una resena existente")
    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<ResenaDTO>> actualizarResena(@PathVariable Integer id, @RequestBody Resena resena) {
        log.info("Solicitud recibida: actualizar resena con ID {}", id);
        ResenaDTO resenaActualizada = resenaService.actualizar(id, resena);

        log.info("Resena con ID {} actualizada correctamente", id);
        return new ResponseEntity<>(assembler.toModel(resenaActualizada), HttpStatus.OK);
    }

    @Operation(summary = "Eliminar resena", description = "Elimina una resena mediante su identificador")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarResena(@PathVariable Integer id) {
        log.info("Solicitud recibida: eliminar resena con ID {}", id);
        String mensaje = resenaService.eliminar(id);

        log.info("Resena con ID {} eliminada correctamente", id);
        return new ResponseEntity<>(mensaje, HttpStatus.OK);
    }
}


