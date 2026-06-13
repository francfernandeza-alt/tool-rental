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

import com.tool_rental.herramientas.DTO.MarcaDTO;
import com.tool_rental.herramientas.Model.Marca;
import com.tool_rental.herramientas.Service.MarcaService;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/marcas")

public class MarcaController {
    @Autowired
    private MarcaService marcaService;

    @GetMapping
    public ResponseEntity<List<MarcaDTO>> todosLasMarcas() {
        List<MarcaDTO> marca = marcaService.obtenerTodos();
        if (marca.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(marca, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MarcaDTO> buscarPorId(@PathVariable Integer id) {
        try {
            MarcaDTO mar = marcaService.buscarporId(id);
            return new ResponseEntity<>(mar, HttpStatus.OK);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<Marca> agregarUsuario(@Valid @RequestBody Marca marca) {
        try {
            Marca guardada = marcaService.guardarMarca(marca);
            return new ResponseEntity<>(guardada, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Marca> editarUsuario(@PathVariable Integer id, @Valid @RequestBody Marca marca) {
        try {
            Marca editada = marcaService.guardarMarca(marca);
            return new ResponseEntity<>(editada, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Marca> actualizarMarca(@PathVariable Integer id, @Valid @RequestBody Marca marca){
        try{
            Marca nuevaMarca = marcaService.actualizarMarca(id, marca);
            return new ResponseEntity<>(nuevaMarca, HttpStatus.OK);
        }catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarMarca(@PathVariable Integer id) {
        String resultado = marcaService.eliminar(id);
        if (resultado.contains("eliminada")) {
            return new ResponseEntity<>(resultado, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(resultado, HttpStatus.NOT_FOUND);
        }
    }
}
