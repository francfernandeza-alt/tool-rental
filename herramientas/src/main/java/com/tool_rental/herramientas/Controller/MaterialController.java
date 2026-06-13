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

import com.tool_rental.herramientas.DTO.MaterialDTO;
import com.tool_rental.herramientas.Model.Material;
import com.tool_rental.herramientas.Service.MaterialService;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/materiales")
public class MaterialController {
    @Autowired
    private MaterialService materialService;

    @GetMapping
    public ResponseEntity<List<MaterialDTO>> todosLosMateriales() {
        List<MaterialDTO> material = materialService.obtenerTodos();
        if (material.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(material, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MaterialDTO> buscarPorId(@PathVariable Integer id) {
        try {
            MaterialDTO material = materialService.buscarPorId(id);
            return new ResponseEntity<>(material, HttpStatus.OK);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<Material> agregarmaterial(@Valid @RequestBody Material material) {
        try {
            Material guardada = materialService.guardarMaterial(material);
            return new ResponseEntity<>(guardada, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Material> editarMaterial(@PathVariable Integer id, @Valid @RequestBody Material material) {
        try {
            Material editada = materialService.guardarMaterial(material);
            return new ResponseEntity<>(editada, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Material> actualizarMaterial(@PathVariable Integer id, @Valid @RequestBody Material material){
        try{
            Material nuevoMaterial = materialService.actualizarMaterial(id, material);
            return new ResponseEntity<>(nuevoMaterial, HttpStatus.OK);
        }catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarMaterial(@PathVariable Integer id) {
        String resultado = materialService.eliminar(id);
        if (resultado.contains("eliminado")) {
            return new ResponseEntity<>(resultado, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(resultado, HttpStatus.NOT_FOUND);
        }
    }
}
