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

import com.tool_rental.reservas.model.TipoReserva;
import com.tool_rental.reservas.service.TipoReservaService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/tipos-reserva")
@Tag(name = "Tipos de reserva", description = "Endpoints para administrar tipos de reserva")
public class TipoReservaController {

    @Autowired
    private TipoReservaService tipoReservaService;

    @Operation(summary = "Listar tipos de reserva")
    @GetMapping
    public ResponseEntity<?> listarTiposReserva() {
        List<TipoReserva> tiposReserva = tipoReservaService.obtenerTodos();

        if (tiposReserva.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(tiposReserva, HttpStatus.OK);
    }

    @Operation(summary = "Buscar tipo de reserva por ID")
    @GetMapping("/{id}")
    public ResponseEntity<?> buscarTipoReserva(@PathVariable Integer id) {
        TipoReserva tipoReserva = tipoReservaService.buscarPorId(id);

        if (tipoReserva == null) {
            return new ResponseEntity<>("Tipo de reserva no encontrado", HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(tipoReserva, HttpStatus.OK);
    }

    @Operation(summary = "Crear tipo de reserva")
    @PostMapping
    public ResponseEntity<?> guardarTipoReserva(@Valid @RequestBody TipoReserva tipoReserva) {
        TipoReserva nuevoTipoReserva = tipoReservaService.guardar(tipoReserva);
        return new ResponseEntity<>(nuevoTipoReserva, HttpStatus.CREATED);
    }

    @Operation(summary = "Actualizar tipo de reserva")
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarTipoReserva(@PathVariable Integer id, @Valid @RequestBody TipoReserva tipoReserva) {
        TipoReserva tipoReservaActualizado = tipoReservaService.actualizar(id, tipoReserva);

        if (tipoReservaActualizado == null) {
            return new ResponseEntity<>("Tipo de reserva no encontrado", HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(tipoReservaActualizado, HttpStatus.OK);
    }

    @Operation(summary = "Eliminar tipo de reserva")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarTipoReserva(@PathVariable Integer id) {
        boolean eliminado = tipoReservaService.eliminar(id);

        if (!eliminado) {
            return new ResponseEntity<>("Tipo de reserva no encontrado", HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>("Tipo de reserva eliminado correctamente", HttpStatus.OK);
    }
}
