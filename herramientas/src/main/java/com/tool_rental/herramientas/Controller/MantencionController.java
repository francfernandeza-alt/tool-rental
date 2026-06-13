package com.tool_rental.herramientas.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tool_rental.herramientas.DTO.HerramientaDTO;
import com.tool_rental.herramientas.DTO.MantencionDTO;
import com.tool_rental.herramientas.Model.Herramienta;
import com.tool_rental.herramientas.Model.Mantencion;
import com.tool_rental.herramientas.Service.MantencionService;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/mantenciones")
public class MantencionController {
    @Autowired
    public MantencionService mantencionService;

    @GetMapping
    public ResponseEntity<List<MantencionDTO>> todasLasMantenciones() {
        List<MantencionDTO> mantencion = mantencionService.findAll();
        if (mantencion.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(mantencion, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MantencionDTO> buscarPorId(@PathVariable Integer id) {
        try {
            MantencionDTO man = mantencionService.findById(id);
            return new ResponseEntity<>(man, HttpStatus.OK);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<Mantencion> agregarMantencion(@Valid @RequestBody Mantencion mantencion) {
        try {
            Mantencion guardada = mantencionService.save(mantencion);
            return new ResponseEntity<>(guardada, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Mantencion> editarMantencion(@PathVariable Integer id, @Valid @RequestBody Mantencion mantencion) {
        try {
            Mantencion editada = mantencionService.save(mantencion);
            return new ResponseEntity<>(editada, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarMantencion(@PathVariable Integer id) {
        String resultado = mantencionService.eliminar(id);
        if (resultado.contains("eliminada")) {
            return new ResponseEntity<>(resultado, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(resultado, HttpStatus.NOT_FOUND);
        }
    }
}
