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

import com.tool_rental.reservas.model.MetodoPago;
import com.tool_rental.reservas.service.MetodoPagoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/metodos-pago")
@Tag(name = "Métodos de pago", description = "Endpoints para administrar métodos de pago")
public class MetodoPagoController {

    @Autowired
    private MetodoPagoService metodoPagoService;

    @Operation(summary = "Listar métodos de pago")
    @GetMapping
    public ResponseEntity<?> listarMetodosPago() {
        List<MetodoPago> metodosPago = metodoPagoService.obtenerTodos();

        if (metodosPago.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(metodosPago, HttpStatus.OK);
    }

    @Operation(summary = "Buscar método de pago por ID")
    @GetMapping("/{id}")
    public ResponseEntity<?> buscarMetodoPago(@PathVariable Integer id) {
        MetodoPago metodoPago = metodoPagoService.buscarPorId(id);

        if (metodoPago == null) {
            return new ResponseEntity<>("Método de pago no encontrado", HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(metodoPago, HttpStatus.OK);
    }

    @Operation(summary = "Crear método de pago")
    @PostMapping
    public ResponseEntity<?> guardarMetodoPago(@Valid @RequestBody MetodoPago metodoPago) {
        MetodoPago nuevoMetodoPago = metodoPagoService.guardar(metodoPago);
        return new ResponseEntity<>(nuevoMetodoPago, HttpStatus.CREATED);
    }

    @Operation(summary = "Actualizar método de pago")
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarMetodoPago(@PathVariable Integer id, @Valid @RequestBody MetodoPago metodoPago) {
        MetodoPago metodoPagoActualizado = metodoPagoService.actualizar(id, metodoPago);

        if (metodoPagoActualizado == null) {
            return new ResponseEntity<>("Método de pago no encontrado", HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(metodoPagoActualizado, HttpStatus.OK);
    }

    @Operation(summary = "Eliminar método de pago")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarMetodoPago(@PathVariable Integer id) {
        boolean eliminado = metodoPagoService.eliminar(id);

        if (!eliminado) {
            return new ResponseEntity<>("Método de pago no encontrado", HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>("Método de pago eliminado correctamente", HttpStatus.OK);
    }
}
