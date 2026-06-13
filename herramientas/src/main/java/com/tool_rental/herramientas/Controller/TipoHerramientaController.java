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

import com.tool_rental.herramientas.DTO.TipoHerramientaDTO;
import com.tool_rental.herramientas.Model.TipoHerramienta;
import com.tool_rental.herramientas.Service.TipoHerramientaService;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/tiposherramientas")
public class TipoHerramientaController {
    @Autowired
    private TipoHerramientaService tipoHerramientaService;

    @GetMapping
    public ResponseEntity<List<TipoHerramientaDTO>> todosLosTiposHerramientas() {
        List<TipoHerramientaDTO> tipoHerramienta = tipoHerramientaService.obtenerTodos();
        if (tipoHerramienta.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(tipoHerramienta, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TipoHerramientaDTO> buscarPorId(@PathVariable Integer id) {
        try {
            TipoHerramientaDTO tipoHerramienta = tipoHerramientaService.buscarPorId(id);
            return new ResponseEntity<>(tipoHerramienta, HttpStatus.OK);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<TipoHerramienta> agregarTipResponseEntity(@Valid @RequestBody TipoHerramienta tipoHerramienta) {
        try {
            TipoHerramienta guardada = tipoHerramientaService.guardarTipoHerramienta(tipoHerramienta);
            return new ResponseEntity<>(guardada, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<TipoHerramienta> editarTipoHerramienta(@PathVariable Integer id, @Valid @RequestBody TipoHerramienta tipoHerramienta) {
        try {
            TipoHerramienta editada = tipoHerramientaService.guardarTipoHerramienta(tipoHerramienta);
            return new ResponseEntity<>(editada, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<TipoHerramienta> actualizarTipoHerramienta(@PathVariable Integer id, @Valid @RequestBody TipoHerramienta tipoHerramienta){
        try{
            TipoHerramienta nuevoTipoHerramienta = tipoHerramientaService.actualizarTipoHerramienta(id, tipoHerramienta);
            return new ResponseEntity<>(nuevoTipoHerramienta, HttpStatus.OK);
        }catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarTipoHerramienta(@PathVariable Integer id) {
        String resultado = tipoHerramientaService.eliminarTipoHerramienta(id);
        if (resultado.contains("eliminado")) {
            return new ResponseEntity<>(resultado, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(resultado, HttpStatus.NOT_FOUND);
        }
    }
}
