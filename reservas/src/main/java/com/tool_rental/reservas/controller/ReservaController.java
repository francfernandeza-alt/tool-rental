package com.tool_rental.reservas.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tool_rental.reservas.assembler.ReservaModelAssembler;
import com.tool_rental.reservas.dto.ReservaDTO;
import com.tool_rental.reservas.model.Reserva;
import com.tool_rental.reservas.service.ReservaService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v1/reservas")
@Tag(name = "Reservas", description = "Endpoints para gestionar reservas de herramientas")
@Slf4j
public class ReservaController {

    @Autowired
    private ReservaService reservaService;

    @Autowired
    private ReservaModelAssembler assembler;

    @Operation(summary = "Listar reservas", description = "Obtiene todas las reservas registradas en el sistema")
    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<ReservaDTO>>> listarReservas() {
        log.info("Solicitud recibida: listar reservas");
        List<ReservaDTO> reservas = reservaService.obtenerTodos();

        if (reservas.isEmpty()) {
            log.warn("No existen reservas registradas");
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        log.info("Reservas listadas correctamente. Total: {}", reservas.size());
        return new ResponseEntity<>(assembler.toCollectionModel(reservas), HttpStatus.OK);
    }

    @Operation(summary = "Buscar reserva por ID", description = "Obtiene una reserva especifica mediante su identificador")
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<ReservaDTO>> buscarReserva(@PathVariable Integer id) {
        log.info("Solicitud recibida: buscar reserva con ID {}", id);
        ReservaDTO reserva = reservaService.buscarPorId(id);

        log.info("Reserva con ID {} encontrada correctamente", id);
        return new ResponseEntity<>(assembler.toModel(reserva), HttpStatus.OK);
    }

    @Operation(summary = "Buscar reservas por usuario", description = "Obtiene todas las reservas asociadas a un RUT de usuario")
    @GetMapping("/usuario/{rutUsuario}")
    public ResponseEntity<CollectionModel<EntityModel<ReservaDTO>>> buscarReservasPorUsuario(@PathVariable String rutUsuario) {
        log.info("Solicitud recibida: buscar reservas del usuario {}", rutUsuario);
        List<ReservaDTO> reservas = reservaService.buscarPorRutUsuario(rutUsuario);

        if (reservas.isEmpty()) {
            log.warn("No existen reservas asociadas al usuario {}", rutUsuario);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        log.info("Reservas del usuario {} obtenidas correctamente. Total: {}", rutUsuario, reservas.size());
        return new ResponseEntity<>(assembler.toCollectionModel(reservas), HttpStatus.OK);
    }

    @Operation(summary = "Crear reserva", description = "Registra una nueva reserva validando fechas, tipo de reserva y metodo de pago")
    @PostMapping
    public ResponseEntity<EntityModel<ReservaDTO>> guardarReserva(@Valid @RequestBody Reserva reserva) {
        log.info("Solicitud recibida: crear reserva para usuario {}", reserva.getRutUsuario());
        ReservaDTO nuevaReserva = reservaService.guardar(reserva);

        log.info("Reserva creada correctamente con ID {}", nuevaReserva.getIdReserva());
        return new ResponseEntity<>(assembler.toModel(nuevaReserva), HttpStatus.CREATED);
    }

    @Operation(summary = "Actualizar reserva", description = "Actualiza los datos de una reserva existente")
    @PatchMapping("/{id}")
    public ResponseEntity<EntityModel<ReservaDTO>> actualizarReserva(@PathVariable Integer id, @RequestBody Reserva reserva) {
        log.info("Solicitud recibida: actualizar reserva con ID {}", id);
        ReservaDTO reservaActualizada = reservaService.actualizar(id, reserva);

        log.info("Reserva con ID {} actualizada correctamente", id);
        return new ResponseEntity<>(assembler.toModel(reservaActualizada), HttpStatus.OK);
    }

    @Operation(summary = "Desactivar reserva", description = "Desactiva una reserva existente mediante su identificador")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarReserva(@PathVariable Integer id) {
        log.info("Solicitud recibida: desactivar reserva con ID {}", id);
        String mensaje = reservaService.desactivar(id);

        log.info("Reserva con ID {} desactivada correctamente", id);
        return new ResponseEntity<>(mensaje, HttpStatus.OK);
    }
}