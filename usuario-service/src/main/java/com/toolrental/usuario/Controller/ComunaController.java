package com.toolrental.usuario.Controller;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.toolrental.usuario.Dto.ComunaDTO;
import com.toolrental.usuario.Model.Comuna;
import com.toolrental.usuario.Service.ComunaService;

@RestController
@RequestMapping("/api/v1/comuna")
public class ComunaController {
  @Autowired
    private ComunaService comunaService;

    @GetMapping
    public ResponseEntity<List<ComunaDTO>> obtenerTodas() {

        List<ComunaDTO> comunas = comunaService.obtenerTodas();

        if (comunas.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(comunas, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ComunaDTO> buscarPorId(
            @PathVariable Integer id) {

        return new ResponseEntity<>(
                comunaService.buscarPorId(id),
                HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Comuna> guardar(
            @RequestBody Comuna comuna) {

        return new ResponseEntity<>(
                comunaService.guardarComuna(comuna),
                HttpStatus.CREATED);
                }


@PutMapping("/{id}")
public ResponseEntity<Comuna> actualizar(
        @PathVariable Integer id,
        @RequestBody Comuna comuna) {

    return new ResponseEntity<>(
            comunaService.actualizarComuna(id, comuna),
            HttpStatus.OK);
}

@DeleteMapping("/{id}")
public ResponseEntity<String> eliminar(@PathVariable Integer id) {

    return new ResponseEntity<>(
            comunaService.eliminar(id),
            HttpStatus.OK);
}
    }


