package com.tool_rental.herramientas.Controller;

import java.util.List;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
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

import com.tool_rental.herramientas.Assemblers.MantencionModelAssembler;
import com.tool_rental.herramientas.DTO.MantencionDTO;
import com.tool_rental.herramientas.Service.MantencionService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v1/mantenciones")
@Slf4j
@Tag(name = "Gestión de Mantenciones", description = "Operaciones para registrar y consultar mantenimientos de herramientas.")

public class MantencionController {
    public final MantencionService mantencionService;

    private final MantencionModelAssembler assembler;

    MantencionController(MantencionService mantencionService, MantencionModelAssembler assembler) {
        this.mantencionService = mantencionService;
        this.assembler = assembler;
    }

    @GetMapping
    @Operation(summary = "Listar mantenciones", description = "Retorna el historial completo de mantenciones en formato DTO.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Historial obtenido con éxito"),
        @ApiResponse(responseCode = "204", description = "No existen registros de mantención")
    })
    public ResponseEntity<CollectionModel<EntityModel<MantencionDTO>>> todasLasMantenciones() {
        log.info("Consultando el listado completo de mantenimientos realizados.");
        List<MantencionDTO> mantenciones = mantencionService.findAll();
        if (mantenciones.isEmpty()) {
            log.info("Consulta completada, no hay mantenciones registradas.");
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(assembler.toCollectionModel(mantenciones), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener una mantencion", description = "Busca y retorna los datos de un registro de mantencion en formato DTO.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Mantenimiento encontrado con éxito"),
        @ApiResponse(responseCode = "404", description = "El código de mantención ingresado no existe")
    })
    public ResponseEntity<EntityModel<MantencionDTO>> buscarPorId(@PathVariable Integer id) {
        log.info("Buscando registro de mantención con código: {}", id);
        MantencionDTO mantencion = mantencionService.findById(id);
        return new ResponseEntity<>(assembler.toModel(mantencion), HttpStatus.OK);
    }

    @PostMapping
    @Operation(summary = "Registrar mantencion", description = "Guarda un nuevo registro de mantención para una herramienta.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "El mantenimiento fue registrado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Los datos enviados son inválidos o incompletos")
    })
    public ResponseEntity<EntityModel<MantencionDTO>> agregarMantencion(@Valid @RequestBody MantencionDTO mantencionDTO) {
        log.info("Iniciando el registro de un nuevo mantenimiento en el sistema.");
        MantencionDTO guardada = mantencionService.save(mantencionDTO);
        return new ResponseEntity<>(assembler.toModel(guardada), HttpStatus.CREATED);
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Editar registro de mantencion", description = "Edita atributos de un registro de mantención existente.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "El registro fue editado correctamente"),
        @ApiResponse(responseCode = "400", description = "Los datos enviados no son válidos"),
        @ApiResponse(responseCode = "404", description = "No se encontró el registro para editar")
    })
    public ResponseEntity<EntityModel<MantencionDTO>> editarMantencion(@PathVariable Integer id, @Valid @RequestBody MantencionDTO mantencionDTO) {
        log.info("Editando registro de mantención con código: {}", id);
        MantencionDTO editada = mantencionService.actualizarMantencion(id, mantencionDTO);
        return new ResponseEntity<>(assembler.toModel(editada), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar mantencion", description = "Actualiza totalmente un registro de mantencion existente segun su ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "La herramienta ha sido reemplazada exitosamente"),
        @ApiResponse(responseCode = "400", description = "Los datos proporcionados no son válidos"),
        @ApiResponse(responseCode = "404", description = "No existe una herramienta con el ID especificado")
    })
    public ResponseEntity<EntityModel<MantencionDTO>> actualizarMantencion(@PathVariable Integer id, @Valid @RequestBody MantencionDTO mantencionDTO) {
        log.info("Actualizando registro de mantencion con ID: {}", id);
        MantencionDTO actualizada = mantencionService.actualizarMantencion(id, mantencionDTO);
        return new ResponseEntity<>(assembler.toModel(actualizada), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Borrar registro de mantención", description = "Elimina un registro de mantenimiento según su ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "El registro de mantención fue eliminado con éxito"),
        @ApiResponse(responseCode = "404", description = "No se encontró un registro con el código proporcionado")
    })
    public ResponseEntity<String> eliminarMantencion(@PathVariable Integer id) {
        log.info("Eliminando registro de mantencion: {}", id);
        String resultado = mantencionService.eliminar(id);
        return new ResponseEntity<>(resultado, HttpStatus.OK);
    }
}
