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
import com.tool_rental.herramientas.Model.Herramienta;
import com.tool_rental.herramientas.Service.HerramientaService;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.validation.Valid;

@RestController
@RequestMapping("api/v1/herramientas")
public class HerramientaController {
    @Autowired
    private HerramientaService herramientaService;
    
    @GetMapping
    public ResponseEntity<List<HerramientaDTO>> todosLasHerramientas() {
        List<HerramientaDTO> herramienta = herramientaService.obtenerTodos();
        if (herramienta.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(herramienta, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<HerramientaDTO> buscarPorId(@PathVariable Integer id) {
        try {
            HerramientaDTO her = herramientaService.buscarPorId(id);
            return new ResponseEntity<>(her, HttpStatus.OK);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<Herramienta> agregarherramienta(@Valid @RequestBody Herramienta herramienta) {
        try {
            Herramienta guardada = herramientaService.guardarHerramienta(herramienta);
            return new ResponseEntity<>(guardada, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Herramienta> editarHerramienta(@PathVariable Integer id, @Valid @RequestBody Herramienta herramienta) {
        try {
            Herramienta editada = herramientaService.guardarHerramienta(herramienta);
            return new ResponseEntity<>(editada, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Herramienta> actualizarHerramienta(@PathVariable Integer id, @Valid @RequestBody Herramienta herramienta){
        try{
            Herramienta newTool = herramientaService.actualizarHerramienta(id, herramienta);
            return new ResponseEntity<>(newTool, HttpStatus.OK);
        }catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarHerramienta(@PathVariable Integer id) {
        String resultado = herramientaService.eliminar(id);
        if (resultado.contains("eliminada")) {
            return new ResponseEntity<>(resultado, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(resultado, HttpStatus.NOT_FOUND);
        }
    }
}
