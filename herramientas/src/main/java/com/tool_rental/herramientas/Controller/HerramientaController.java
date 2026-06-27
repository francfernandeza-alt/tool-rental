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

import com.tool_rental.herramientas.Assemblers.HerramientaModelAssembler;
import com.tool_rental.herramientas.DTO.HerramientaDTO;
import com.tool_rental.herramientas.Service.HerramientaService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("api/v1/herramientas")
@Slf4j
@Tag(name = "Herramientas", description = "Endpoints para la gestión del inventario y stock de herramientas.")
public class HerramientaController {
    private final HerramientaService herramientaService;

    private final HerramientaModelAssembler assembler;

    HerramientaController(HerramientaService herramientaService, HerramientaModelAssembler assembler) {
        this.herramientaService = herramientaService;
        this.assembler = assembler;
    }
    
    @GetMapping
    @Operation(summary = "Obtener todas las herramientas", description = ("Retorna una lista de todas las herramientas en formato DTO"))
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Listado de herramientas obtenido con éxito"),
        @ApiResponse(responseCode = "204", description = "La consulta fue exitosa pero no hay herramientas registradas")
    })
    public ResponseEntity<CollectionModel<EntityModel<HerramientaDTO>>> todosLasHerramientas() {
        log.info("Consultando el listado completo de herramientas.");
        List<HerramientaDTO> herramienta = herramientaService.obtenerTodos();
        if (herramienta.isEmpty()) {
            log.info("Consulta completada, no existen registros para mostrar.");
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        log.info("Se listaron {} herramientas.", herramienta.size());
        return new ResponseEntity<>(assembler.toCollectionModel(herramienta), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener un Herramienta", description = ("Busca y retorna una herramienta en formato DTO según un ID dado.") )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Herramienta encontrada"),
        @ApiResponse(responseCode = "404", description = "La herramienta no existe")
    })
    public ResponseEntity<EntityModel<HerramientaDTO>> buscarPorId(@PathVariable Integer id) {
        log.info("Buscando Herramienta con el código: {}", id);
        HerramientaDTO her = herramientaService.buscarPorId(id);
        return new ResponseEntity<>(assembler.toModel(her), HttpStatus.OK);
    }

    @PostMapping
    @Operation(summary = "Agregar una nueva herramienta", description = "Guarda una nueva herramienta en el inventario.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "La herramienta fue registrada exitosamente"),
        @ApiResponse(responseCode = "400", description = "Los datos enviados son inválidos o están incompletos")
    })
    public ResponseEntity<EntityModel<HerramientaDTO>> agregarherramienta(@Valid @RequestBody HerramientaDTO herramientaDTO) {
        log.info("Registrando la herramienta {}", herramientaDTO.getNombreHerramientaDTO());
        HerramientaDTO guardada = herramientaService.guardarHerramienta(herramientaDTO);
        return new ResponseEntity<>(assembler.toModel(guardada), HttpStatus.CREATED);
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Actualizar herramienta", description = "Modifica los datos de una herramienta existente según su ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "La herramienta fue editada correctamente"),
        @ApiResponse(responseCode = "400", description = "Los datos de edición son inválidos"),
        @ApiResponse(responseCode = "404", description = "No se encontró la herramienta para editar")
    })
    public ResponseEntity<EntityModel<HerramientaDTO>> editarHerramienta(@PathVariable Integer id, @Valid @RequestBody HerramientaDTO herramientaDTO) {
        log.info("Editando herramienta con código: {}", id);
        HerramientaDTO editada = herramientaService.actualizarHerramienta(id, herramientaDTO);
        return new ResponseEntity<>(assembler.toModel(editada), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualización total de una herramienta", description = "Reemplaza todos los datos de una herramienta existente en el inventario.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "La herramienta fue actualizada exitosamente"),
        @ApiResponse(responseCode = "400", description = "Los datos de entrada no cumplen con las validaciones"),
        @ApiResponse(responseCode = "404", description = "No se encontró la herramienta con el ID proporcionado")
    })
    public ResponseEntity<EntityModel<HerramientaDTO>> actualizarHerramienta(@PathVariable Integer id, @Valid @RequestBody HerramientaDTO herramientaDTO){
        log.info("Actualizando la herramienta con código: {}", id);
        HerramientaDTO actualizada = herramientaService.actualizarHerramienta(id, herramientaDTO);
        return new ResponseEntity<>(assembler.toModel(actualizada), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar herramienta", description = "Elimina una herramienta del sistema usando su ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "La herramienta fue eliminada correctamente"),
        @ApiResponse(responseCode = "404", description = "El código ingresado no corresponde a ninguna herramienta")
    })
    public ResponseEntity<String> eliminarHerramienta(@PathVariable Integer id) {
        log.info("Eliminando herramienta con código: {}", id);
        String resultado = herramientaService.eliminar(id);
        return new ResponseEntity<>(resultado, HttpStatus.OK);
    }
}
