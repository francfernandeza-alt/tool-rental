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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v1/marcas")
@Slf4j
@Tag(name = "Gestión de Marcas", description = "Endpoints para administrar las marcas de las herramientas.")
public class MarcaController {
    @Autowired
    private MarcaService marcaService;

    @GetMapping
    @Operation(summary = "Listar marcas", description = "Retorna el listado completo de marcas registradas en formato DTO.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Listado obtenido con éxito"),
        @ApiResponse(responseCode = "204", description = "No hay marcas registradas en el sistema")
    })
    public ResponseEntity<List<MarcaDTO>> todosLasMarcas() {
        log.info("Consultando el listado completo de marcas de herramientas.");
        List<MarcaDTO> marca = marcaService.obtenerTodos();
        if (marca.isEmpty()) {
            log.info("La consulta finalizó sin encontrar registros.");
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(marca, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener una marca", description = "Busca y retorna la información de una marca específica en formato DTO.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Marca encontrada exitosamente"),
        @ApiResponse(responseCode = "404", description = "No existe una marca con el ID proporcionado")
    })
    public ResponseEntity<MarcaDTO> buscarPorId(@PathVariable Integer id) {
        log.info("Buscando marca con código: {}", id);
            MarcaDTO marcaDTO = marcaService.buscarporId(id);
            return new ResponseEntity<>(marcaDTO, HttpStatus.OK);
    }

    @PostMapping
    @Operation(summary = "Registrar nueva marca", description = "Guarda una nueva marca en el sistema utilizando un formato DTO.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "La marca fue registrada exitosamente"),
        @ApiResponse(responseCode = "400", description = "Los datos enviados son inválidos o incompletos")
    })
    public ResponseEntity<MarcaDTO> agregarMarca(@Valid @RequestBody MarcaDTO marcaDTO) {
        log.info("Registrando nueva marca.");
        MarcaDTO guardada = marcaService.guardarMarca(marcaDTO);
        return new ResponseEntity<>(guardada, HttpStatus.CREATED);
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Editar marca", description = "Edita atributos específicos de una marca existente según su ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "La marca fue modificada exitosamente"),
        @ApiResponse(responseCode = "400", description = "Los datos enviados no cumplen las validaciones"),
        @ApiResponse(responseCode = "404", description = "No se encontró la marca con el código proporcionado")
    })
    public ResponseEntity<MarcaDTO> editarMarca(@PathVariable Integer id, @Valid @RequestBody MarcaDTO marcaDTO) {
        log.info("Editando la marca con código: {}", id);
        MarcaDTO editada = marcaService.actualizarMarca(id, marcaDTO);
        return new ResponseEntity<>(editada, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar marca", description = "Actualiza totalmente una marca existente según su ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "La marca ha sido reemplazada exitosamente"),
        @ApiResponse(responseCode = "400", description = "Los datos proporcionados no cumplen las validaciones"),
        @ApiResponse(responseCode = "404", description = "No se encontró la marca con el código ingresado")
    })
    public ResponseEntity<MarcaDTO> actualizarMarca(@PathVariable Integer id, @Valid @RequestBody MarcaDTO marcaDTO){
        log.info("Actualizando la marca con código: {}", id);
        MarcaDTO actualizada = marcaService.actualizarMarca(id, marcaDTO);
        return new ResponseEntity<>(actualizada, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar marca", description = "Elimina una marca según su ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "La marca fue eliminada exitosamente"),
        @ApiResponse(responseCode = "404", description = "El código ingresado no pertenece a ninguna marca registrada")
    })
    public ResponseEntity<String> eliminarMarca(@PathVariable Integer id) {
        log.info("Eliminando la marca con código: {}", id);
        String resultado = marcaService.eliminar(id);
        return new ResponseEntity<>(resultado, HttpStatus.OK);
    }
}
