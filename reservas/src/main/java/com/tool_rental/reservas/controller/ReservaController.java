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

import com.tool_rental.reservas.dto.ReservaDTO;
import com.tool_rental.reservas.model.Reserva;
import com.tool_rental.reservas.service.ReservaService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/reservas")
@Tag(name = "Reservas", description = "Endpoints para gestionar reservas de herramientas")
public class ReservaController {

    @Autowired
    private ReservaService reservaService;

    @Operation(summary = "Listar reservas", description = "Obtiene todas las reservas registradas en el sistema")
    @GetMapping
    public ResponseEntity<?> listarReservas() {
        List<ReservaDTO> reservas = reservaService.obtenerTodos();

        if (reservas.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(reservas, HttpStatus.OK);
    }

    @Operation(summary = "Buscar reserva por ID", description = "Obtiene una reserva específica mediante su identificador")
    @GetMapping("/{id}")
    public ResponseEntity<?> buscarReserva(@PathVariable Integer id) {
        ReservaDTO reserva = reservaService.buscarPorId(id);
        return new ResponseEntity<>(reserva, HttpStatus.OK);
    }

    @Operation(summary = "Buscar reservas por usuario", description = "Obtiene todas las reservas asociadas a un RUT de usuario")
    @GetMapping("/usuario/{rutUsuario}")
    public ResponseEntity<?> buscarReservasPorUsuario(@PathVariable String rutUsuario) {
        List<ReservaDTO> reservas = reservaService.buscarPorRutUsuario(rutUsuario);

        if (reservas.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(reservas, HttpStatus.OK);
    }

    @Operation(summary = "Crear reserva", description = "Registra una nueva reserva validando fechas, tipo de reserva y método de pago")
    @PostMapping
    public ResponseEntity<?> guardarReserva(@Valid @RequestBody Reserva reserva) {
        ReservaDTO nuevaReserva = reservaService.guardar(reserva);
        return new ResponseEntity<>(nuevaReserva, HttpStatus.CREATED);
    }

    @Operation(summary = "Actualizar reserva", description = "Actualiza los datos de una reserva existente")
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarReserva(@PathVariable Integer id, @RequestBody Reserva reserva) {
        ReservaDTO reservaActualizada = reservaService.actualizar(id, reserva);
        return new ResponseEntity<>(reservaActualizada, HttpStatus.OK);
    }

    @Operation(summary = "Eliminar reserva", description = "Elimina una reserva existente mediante su identificador")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarReserva(@PathVariable Integer id) {
        String mensaje = reservaService.eliminar(id);
        return new ResponseEntity<>(mensaje, HttpStatus.OK);
    }
}
