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

import com.tool_rental.herramientas.Assemblers.TipoHerramientaModelAssembler;
import com.tool_rental.herramientas.DTO.TipoHerramientaDTO;
import com.tool_rental.herramientas.Service.TipoHerramientaService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v1/tiposherramientas")
@Slf4j
@Tag(name = "Tipos de Herramienta", description = "Gestión de categorías de herramientas")
public class TipoHerramientaController {
    private final TipoHerramientaService tipoHerramientaService;

    private final TipoHerramientaModelAssembler assembler;

    TipoHerramientaController(TipoHerramientaModelAssembler assembler, TipoHerramientaService tipoHerramientaService) {
        this.assembler = assembler;
        this.tipoHerramientaService = tipoHerramientaService;
    }

    @GetMapping
    @Operation(summary = "Listar categorías", description = "Retorna el listado completo de tipos de herramienta en formato DTO.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Listado obtenido con éxito"),
        @ApiResponse(responseCode = "204", description = "No hay registros en el sistema")
    })
    public ResponseEntity<CollectionModel<EntityModel<TipoHerramientaDTO>>> todosLosTiposHerramientas() {
        log.info("Consultando el listado completo de tipos de herramientas.");
        List<TipoHerramientaDTO> tipoHerramienta = tipoHerramientaService.findAll();
        log.info("Listando todos los tipos de herramienta.");
        if (tipoHerramienta.isEmpty()) {
            log.info("Consulta completada, no existen registros para mostrar.");
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        log.info("Se listaron {} tipos de herramienta.", tipoHerramienta.size());
        return new ResponseEntity<>(assembler.toCollectionModel(tipoHerramienta), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar tipo de herramienta por ID", description = "Busca y retorna un tipo de herramienta  según su ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Tipo de herramienta encontrada exitosamente"),
        @ApiResponse(responseCode = "404", description = "El ID proporcionado no existe")
    })
    public ResponseEntity<EntityModel<TipoHerramientaDTO>> buscarPorId(@PathVariable Integer id) {
        log.info("Obteniendo tipo de herramienta con ID: {}", id);
        TipoHerramientaDTO tipoHerramientaDTO = tipoHerramientaService.findById(id);
        return new ResponseEntity<>(assembler.toModel(tipoHerramientaDTO), HttpStatus.OK);
    }

    @PostMapping
    @Operation(summary = "Registrar nuevo tipo de herramienta", description = "Guarda un nuevo tipo de herramienta utilizando el formato DTO.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "El tipo de herramienta fue registrado con éxito"),
        @ApiResponse(responseCode = "400", description = "Los datos enviados no son válidos")
    })
    public ResponseEntity<EntityModel<TipoHerramientaDTO>> agregarTipo(@Valid @RequestBody TipoHerramientaDTO tipoHerramientaDTO) {
        log.info("Agregando nuevo tipo de herramienta.");
        TipoHerramientaDTO guardada = tipoHerramientaService.save(tipoHerramientaDTO);
        return new ResponseEntity<>(assembler.toModel(guardada), HttpStatus.CREATED);
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Editar tipo de herramienta", description = "Edita atributos específicos de un tipo de herramienta existente según su ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "El tipo de herramienta fue editado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Los datos enviados no cumplen las validaciones"),
        @ApiResponse(responseCode = "404", description = "No se encontró el tipo de herramienta con el ID ingresado")
    })
    public ResponseEntity<EntityModel<TipoHerramientaDTO>> editarTipoHerramienta(@PathVariable Integer id, @Valid @RequestBody TipoHerramientaDTO tipoHerramientaDTO) {
        log.info("Editando el tipo de herramienta con ID: {}", id);
        TipoHerramientaDTO editado = tipoHerramientaService.actualizarTipoHerramienta(id, tipoHerramientaDTO);
        return new ResponseEntity<>(assembler.toModel(editado), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualización de tipo de herramienta", description = "Actualiza totalmente un tipo de herramienta existente.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "El tipo de herramienta ha sido actualizado correctamente"),
        @ApiResponse(responseCode = "404", description = "No se encontró el tipo de herramienta para actualizar")
    })
    public ResponseEntity<EntityModel<TipoHerramientaDTO>> actualizarTipoHerramienta(@PathVariable Integer id, @Valid @RequestBody TipoHerramientaDTO tipoHerramientaDTO){
        log.info("Actualizando el tipo de herramienta con Id: {}", id);
        TipoHerramientaDTO actualizado = tipoHerramientaService.actualizarTipoHerramienta(id, tipoHerramientaDTO);
        return new ResponseEntity<>(assembler.toModel(actualizado), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar tipo de herramienta", description = "Elimina un tipo de herramienta del sistema.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "El tipo de herramienta fue eliminado exitosamente"),
        @ApiResponse(responseCode = "404", description = "El ID ingresado no existe")
    })
    public ResponseEntity<String> eliminar(@PathVariable Integer id) {
        log.info("Eliminando el tipo de herramienta con ID: {}", id);
        String resultado = tipoHerramientaService.eliminar(id);
        return new ResponseEntity<>(resultado, HttpStatus.OK);
    }
}
