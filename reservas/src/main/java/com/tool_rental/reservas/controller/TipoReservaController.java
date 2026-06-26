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

import com.tool_rental.reservas.Assembler.TipoReservaModelAssembler;
import com.tool_rental.reservas.model.TipoReserva;
import com.tool_rental.reservas.service.TipoReservaService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v1/tipos-reserva")
@Tag(name = "Tipos de reserva", description = "Endpoints para administrar tipos de reserva")
@Slf4j
public class TipoReservaController {

    @Autowired
    private TipoReservaService tipoReservaService;

    @Autowired
    private TipoReservaModelAssembler assembler;

    @Operation(summary = "Listar tipos de reserva")
    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<TipoReserva>>> listarTiposReserva() {
        log.info("Solicitud recibida: listar tipos de reserva");
        List<TipoReserva> tiposReserva = tipoReservaService.obtenerTodos();

        if (tiposReserva.isEmpty()) {
            log.warn("No existen tipos de reserva registrados");
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        log.info("Tipos de reserva listados correctamente");
        return new ResponseEntity<>(assembler.toCollectionModel(tiposReserva), HttpStatus.OK);
    }

    @Operation(summary = "Buscar tipo de reserva por ID")
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<TipoReserva>> buscarTipoReserva(@PathVariable Integer id) {
        log.info("Solicitud recibida: buscar tipo de reserva con ID {}", id);
        TipoReserva tipoReserva = tipoReservaService.buscarPorId(id);

        if (tipoReserva == null) {
            log.warn("Tipo de reserva con ID {} no encontrado", id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        log.info("Tipo de reserva con ID {} encontrado correctamente", id);
        return new ResponseEntity<>(assembler.toModel(tipoReserva), HttpStatus.OK);
    }

    @Operation(summary = "Crear tipo de reserva")
    @PostMapping
    public ResponseEntity<EntityModel<TipoReserva>> guardarTipoReserva(@Valid @RequestBody TipoReserva tipoReserva) {
        log.info("Solicitud recibida: crear tipo de reserva");
        TipoReserva nuevoTipoReserva = tipoReservaService.guardar(tipoReserva);

        log.info("Tipo de reserva creado correctamente con ID {}", nuevoTipoReserva.getIdTipoReserva());
        return new ResponseEntity<>(assembler.toModel(nuevoTipoReserva), HttpStatus.CREATED);
    }

    @Operation(summary = "Actualizar tipo de reserva")
    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<TipoReserva>> actualizarTipoReserva(@PathVariable Integer id, @Valid @RequestBody TipoReserva tipoReserva) {
        log.info("Solicitud recibida: actualizar tipo de reserva con ID {}", id);
        TipoReserva tipoReservaActualizado = tipoReservaService.actualizar(id, tipoReserva);

        if (tipoReservaActualizado == null) {
            log.warn("No se pudo actualizar. Tipo de reserva con ID {} no existe", id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        log.info("Tipo de reserva con ID {} actualizado correctamente", id);
        return new ResponseEntity<>(assembler.toModel(tipoReservaActualizado), HttpStatus.OK);
    }

    @Operation(summary = "Eliminar tipo de reserva")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarTipoReserva(@PathVariable Integer id) {
        log.info("Solicitud recibida: eliminar tipo de reserva con ID {}", id);
        boolean eliminado = tipoReservaService.eliminar(id);

        if (!eliminado) {
            log.warn("No se pudo eliminar. Tipo de reserva con ID {} no existe", id);
            return new ResponseEntity<>("Tipo de reserva no encontrado", HttpStatus.NOT_FOUND);
        }

        log.info("Tipo de reserva con ID {} eliminado correctamente", id);
        return new ResponseEntity<>("Tipo de reserva eliminado correctamente", HttpStatus.OK);
    }
}
