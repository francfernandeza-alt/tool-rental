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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v1/materiales")
@Slf4j
@Tag(name = "Materiales", description = "Endpoints para la gestión de materiales asociados a herramientas.")
public class MaterialController {
    @Autowired
    private MaterialService materialService;

    @GetMapping
    @Operation(summary = "Listar materiales", description = "Retorna todos los materiales registrados en formato DTO.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Listado obtenido con éxito"),
        @ApiResponse(responseCode = "204", description = "No existen registros de materiales en el sistema")
    })
    public ResponseEntity<List<MaterialDTO>> todosLosMateriales() {
        log.info("Iniciando la consulta del catálogo completo de materiales.");
        List<MaterialDTO> material = materialService.obtenerTodos();
        if (material.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(material, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar un material", description = "Busca y retorna un material específico en formato DTO.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Material encontrado exitosamente"),
        @ApiResponse(responseCode = "404", description = "El ID ingresado no corresponde a ningún material registrado")
    })
    public ResponseEntity<MaterialDTO> buscarPorId(@PathVariable Integer id) {
        log.info("Consultando la información detallada para el material con código: {}", id);
        MaterialDTO materialDTO = materialService.buscarPorId(id);
        return new ResponseEntity<>(materialDTO, HttpStatus.OK);
    }

    @PostMapping
    @Operation(summary = "Guardar material", description = "Guarda un nuevo material usando un formato DTO.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "El material fue registrado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Los datos enviados son inválidos o incompletos")
    })
    public ResponseEntity<MaterialDTO> agregarmaterial(@Valid @RequestBody MaterialDTO materialDTO) {
        log.info("Registrando nuevo material en el sistema.");
        MaterialDTO guardada = materialService.guardarMaterial(materialDTO);
        return new ResponseEntity<>(guardada, HttpStatus.CREATED);
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Editar material", description = "Edita atributos específicos de un registro de material existente según su ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "El material fue editado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Los datos enviados no cumplen las validaciones"),
        @ApiResponse(responseCode = "404", description = "No se encontró el material con el ID ingresado")
    })
    public ResponseEntity<MaterialDTO> editarMaterial(@PathVariable Integer id, @Valid @RequestBody MaterialDTO materialDTO) {
        log.info("Editando el material con código: {}", id);
        MaterialDTO editado = materialService.guardarMaterial(materialDTO);
        return new ResponseEntity<>(editado, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar material", description = "Actualiza totalmente un material existente según su ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "El material ha sido actualizadp exitosamente"),
        @ApiResponse(responseCode = "400", description = "Los datos proporcionados no cumplen las validaciones"),
        @ApiResponse(responseCode = "404", description = "No se encontró el material con el ID ingresado")
    })
    public ResponseEntity<MaterialDTO> actualizarMaterial(@PathVariable Integer id, @Valid @RequestBody MaterialDTO materialDTO){
        log.info("Actualizando el material con código: {}", id);
        MaterialDTO actualizado = materialService.actualizarMaterial(id, materialDTO);
        return new ResponseEntity<>(actualizado, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar material", description = "Elimina un material según su ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "El material fue eliminado exitosamente"),
        @ApiResponse(responseCode = "404", description = "No se encontró un material con el código proporcionado")
    })
    public ResponseEntity<String> eliminarMaterial(@PathVariable Integer id) {
        log.info("PEliminando el material con código: {}", id);
        String resultado = materialService.eliminar(id);
        return new ResponseEntity<>(resultado, HttpStatus.OK);
    }
}
