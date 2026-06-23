package com.tool_rental.reservas.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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

import com.tool_rental.reservas.dto.ResenaDTO;
import com.tool_rental.reservas.model.Resena;
import com.tool_rental.reservas.service.ResenaService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/resenas")
@Tag(name = "Reseñas", description = "Endpoints para gestionar reseñas asociadas a reservas y herramientas")
public class ResenaController {

    @Autowired
    private ResenaService resenaService;

    @Operation(summary = "Listar reseñas", description = "Obtiene todas las reseñas registradas")
    @GetMapping
    public ResponseEntity<?> listarResenas() {
        List<ResenaDTO> resenas = resenaService.obtenerTodos();

        if (resenas.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(resenas, HttpStatus.OK);
    }

    @Operation(summary = "Buscar reseña por ID", description = "Obtiene una reseña mediante su identificador")
    @GetMapping("/{id}")
    public ResponseEntity<?> buscarResena(@PathVariable Integer id) {
        ResenaDTO resena = resenaService.buscarPorId(id);
        return new ResponseEntity<>(resena, HttpStatus.OK);
    }

    @Operation(summary = "Buscar reseñas por usuario", description = "Obtiene las reseñas asociadas a un RUT de usuario")
    @GetMapping("/usuario/{rutUsuario}")
    public ResponseEntity<?> buscarResenasPorUsuario(@PathVariable String rutUsuario) {
        List<ResenaDTO> resenas = resenaService.buscarPorRutUsuario(rutUsuario);

        if (resenas.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(resenas, HttpStatus.OK);
    }

    @Operation(summary = "Buscar reseñas por herramienta", description = "Obtiene las reseñas asociadas a una herramienta")
    @GetMapping("/herramienta/{herramientaId}")
    public ResponseEntity<?> buscarResenasPorHerramienta(@PathVariable Integer herramientaId) {
        List<ResenaDTO> resenas = resenaService.buscarPorHerramientaId(herramientaId);

        if (resenas.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(resenas, HttpStatus.OK);
    }

    @Operation(summary = "Buscar reseñas por reserva", description = "Obtiene las reseñas asociadas a una reserva")
    @GetMapping("/reserva/{reservaId}")
    public ResponseEntity<?> buscarResenasPorReserva(@PathVariable Integer reservaId) {
        List<ResenaDTO> resenas = resenaService.buscarPorReservaId(reservaId);

        if (resenas.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(resenas, HttpStatus.OK);
    }

    @Operation(summary = "Crear reseña", description = "Registra una reseña validando puntuación, reserva y herramienta")
    @PostMapping
    public ResponseEntity<?> guardarResena(@Valid @RequestBody Resena resena) {
        ResenaDTO nuevaResena = resenaService.guardar(resena);
        return new ResponseEntity<>(nuevaResena, HttpStatus.CREATED);
    }

    @Operation(summary = "Actualizar reseña", description = "Actualiza una reseña existente")
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarResena(@PathVariable Integer id, @RequestBody Resena resena) {
        ResenaDTO resenaActualizada = resenaService.actualizar(id, resena);
        return new ResponseEntity<>(resenaActualizada, HttpStatus.OK);
    }

    @Operation(summary = "Eliminar reseña", description = "Elimina una reseña mediante su identificador")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarResena(@PathVariable Integer id) {
        String mensaje = resenaService.eliminar(id);
        return new ResponseEntity<>(mensaje, HttpStatus.OK);
    }
}
