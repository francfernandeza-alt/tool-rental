package com.toolrental.usuario.Controller;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.multiherramienta.multiherramienta.DTO.DireccionDTO;
import com.multiherramienta.multiherramienta.Model.Direccion;
import com.multiherramienta.multiherramienta.Services.DireccionService;




@RestController
@RequestMapping("/api/v1/direccion")
public class DireccionController {
 @Autowired
    private DireccionService direccionService;

    @GetMapping
    public ResponseEntity<List<DireccionDTO>> todosLasDirecciones() {
        List<DireccionDTO> direccion = direccionService.obtenerTodas();
        if (direccion.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(direccion, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DireccionDTO> buscarPorId(@PathVariable Integer id) {
        try {
            DireccionDTO direccion = direccionService.buscarPorId(id);
            return new ResponseEntity<>(direccion, HttpStatus.OK);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<Direccion> agregarDireccion(@RequestBody Direccion direccion) {
        try {
            Direccion guardada = direccionService.guardaDireccion(direccion);
            return new ResponseEntity<>(guardada, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Direccion> editarDireccion(@PathVariable Integer id, @RequestBody Direccion direccion) {
        try {
            Direccion editada = direccionService.guardaDireccion(direccion);
            return new ResponseEntity<>(editada, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Direccion> actualizarDireccion(@PathVariable Integer id, @RequestBody Direccion direccion){
        try{
            Direccion nuevaDireccion = direccionService.actualizarDireccion(id, direccion);
            return new ResponseEntity<>(nuevaDireccion, HttpStatus.OK);
        }catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarDireccion(@PathVariable Integer id) {
        String resultado = direccionService.eliminar(id);
        if (resultado.contains("eliminada")) {
            return new ResponseEntity<>(resultado, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(resultado, HttpStatus.NOT_FOUND);
        }
    }
}
