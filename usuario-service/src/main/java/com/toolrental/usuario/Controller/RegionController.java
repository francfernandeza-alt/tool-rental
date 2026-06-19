package com.toolrental.usuario.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.toolrental.usuario.Dto.RegionDTO;
import com.toolrental.usuario.Model.Region;
import com.toolrental.usuario.Service.RegionService;
@RestController
@RequestMapping("/api/v1/region")
public class RegionController {
@Autowired
    private RegionService regionService;

    @GetMapping
    public ResponseEntity<List<RegionDTO>> obtenerTodas() {

        List<RegionDTO> regiones =
                regionService.obtenerTodas();

        if (regiones.isEmpty()) {
            return new ResponseEntity<>(
                    HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(
                regiones,
                HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RegionDTO> buscarPorId(
            @PathVariable Integer id) {

        return new ResponseEntity<>(
                regionService.buscarPorId(id),
                HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Region> guardar(
            @RequestBody Region region) {

        return new ResponseEntity<>(
                regionService.guardar(region),
                HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Region> actualizar(
            @PathVariable Integer id,
            @RequestBody Region region) {

        return new ResponseEntity<>(
                regionService.actualizar(id, region),
                HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminar(
            @PathVariable Integer id) {

        return new ResponseEntity<>(
                regionService.eliminar(id),
       HttpStatus.OK);
    }
}
