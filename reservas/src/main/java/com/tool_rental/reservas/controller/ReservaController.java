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

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/reservas")
public class ReservaController {

    @Autowired
    private ReservaService reservaService;

    @GetMapping
    public ResponseEntity<?> listarReservas() {
        List<ReservaDTO> reservas = reservaService.obtenerTodos();

        if (reservas.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(reservas, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarReserva(@PathVariable Integer id) {
        ReservaDTO reserva = reservaService.buscarPorId(id);

        if (reserva == null) {
            return new ResponseEntity<>("Reserva no encontrada", HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(reserva, HttpStatus.OK);
    }

    @GetMapping("/usuario/{rutUsuario}")
    public ResponseEntity<?> buscarReservasPorUsuario(@PathVariable String rutUsuario) {
        List<ReservaDTO> reservas = reservaService.buscarPorRutUsuario(rutUsuario);

        if (reservas.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(reservas, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> guardarReserva(@Valid @RequestBody Reserva reserva) {
        ReservaDTO nuevaReserva = reservaService.guardar(reserva);

        if (nuevaReserva == null) {
            return new ResponseEntity<>("No se pudo crear la reserva. Verifique fechas, tipo de reserva y método de pago.", HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(nuevaReserva, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarReserva(@PathVariable Integer id, @RequestBody Reserva reserva) {
        ReservaDTO reservaActualizada = reservaService.actualizar(id, reserva);

        if (reservaActualizada == null) {
            return new ResponseEntity<>("No se pudo actualizar la reserva. Verifique datos ingresados.", HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(reservaActualizada, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarReserva(@PathVariable Integer id) {
        boolean eliminado = reservaService.eliminar(id);

        if (!eliminado) {
            return new ResponseEntity<>("Reserva no encontrada", HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>("Reserva eliminada correctamente", HttpStatus.OK);
    }
}
