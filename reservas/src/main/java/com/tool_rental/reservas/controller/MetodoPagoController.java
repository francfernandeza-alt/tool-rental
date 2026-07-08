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

import com.tool_rental.reservas.assembler.MetodoPagoModelAssembler;
import com.tool_rental.reservas.model.MetodoPago;
import com.tool_rental.reservas.service.MetodoPagoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v1/metodos-pago")
@Tag(name = "Metodos de pago", description = "Endpoints para administrar metodos de pago")
@Slf4j
public class MetodoPagoController {

    @Autowired
    private MetodoPagoService metodoPagoService;

    @Autowired
    private MetodoPagoModelAssembler assembler;

    @Operation(summary = "Listar metodos de pago")
    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<MetodoPago>>> listarMetodosPago() {
        log.info("Solicitud recibida: listar metodos de pago");
        List<MetodoPago> metodosPago = metodoPagoService.obtenerTodos();

        if (metodosPago.isEmpty()) {
            log.warn("No existen metodos de pago registrados");
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        log.info("Metodos de pago listados correctamente");
        return new ResponseEntity<>(assembler.toCollectionModel(metodosPago), HttpStatus.OK);
    }

    @Operation(summary = "Buscar metodo de pago por ID")
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<MetodoPago>> buscarMetodoPago(@PathVariable Integer id) {
        log.info("Solicitud recibida: buscar metodo de pago con ID {}", id);
        MetodoPago metodoPago = metodoPagoService.buscarPorId(id);

        if (metodoPago == null) {
            log.warn("Metodo de pago con ID {} no encontrado", id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        log.info("Metodo de pago con ID {} encontrado correctamente", id);
        return new ResponseEntity<>(assembler.toModel(metodoPago), HttpStatus.OK);
    }

    @Operation(summary = "Crear metodo de pago")
    @PostMapping
    public ResponseEntity<EntityModel<MetodoPago>> guardarMetodoPago(@Valid @RequestBody MetodoPago metodoPago) {
        log.info("Solicitud recibida: crear metodo de pago");
        MetodoPago nuevoMetodoPago = metodoPagoService.guardar(metodoPago);

        log.info("Metodo de pago creado correctamente con ID {}", nuevoMetodoPago.getIdMetodoPago());
        return new ResponseEntity<>(assembler.toModel(nuevoMetodoPago), HttpStatus.CREATED);
    }

    @Operation(summary = "Actualizar metodo de pago")
    @PatchMapping("/{id}")
    public ResponseEntity<EntityModel<MetodoPago>> actualizarMetodoPago(@PathVariable Integer id, @Valid @RequestBody MetodoPago metodoPago) {
        log.info("Solicitud recibida: actualizar metodo de pago con ID {}", id);
        MetodoPago metodoPagoActualizado = metodoPagoService.actualizar(id, metodoPago);

        if (metodoPagoActualizado == null) {
            log.warn("No se pudo actualizar. Metodo de pago con ID {} no existe", id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        log.info("Metodo de pago con ID {} actualizado correctamente", id);
        return new ResponseEntity<>(assembler.toModel(metodoPagoActualizado), HttpStatus.OK);
    }

    @Operation(summary = "Desactivar metodo de pago")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarMetodoPago(@PathVariable Integer id) {
        log.info("Solicitud recibida: desactivar metodo de pago con ID {}", id);
        boolean eliminado = metodoPagoService.desactivar(id);

        if (!eliminado) {
            log.warn("No se pudo desactivar. Metodo de pago con ID {} no existe", id);
            return new ResponseEntity<>("Metodo de pago no encontrado", HttpStatus.NOT_FOUND);
        }

        log.info("Metodo de pago con ID {} desactivado correctamente", id);
        return new ResponseEntity<>("Metodo de pago desactivado correctamente", HttpStatus.OK);
    }
}