package com.tool_rental.herramientas.Assemblers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.tool_rental.herramientas.Controller.TipoHerramientaController;
import com.tool_rental.herramientas.DTO.TipoHerramientaDTO;
@Component
public class TipoHerramientaModelAssembler implements RepresentationModelAssembler <TipoHerramientaDTO, EntityModel<TipoHerramientaDTO>>{
    @SuppressWarnings("null")
    @Override
    public EntityModel <TipoHerramientaDTO> toModel(TipoHerramientaDTO tipoHerramientaDTO) {
        Integer id = tipoHerramientaDTO.getIdTipoHerramientaDTO();
        return EntityModel.of(tipoHerramientaDTO,
            linkTo(methodOn(TipoHerramientaController.class).buscarPorId(id)).withSelfRel(),
            linkTo(methodOn(TipoHerramientaController.class).todosLosTiposHerramientas()).withRel("tipos-herramientas"),
            linkTo(methodOn(TipoHerramientaController.class).agregarTipo(null)).withRel("guardar"),
            linkTo(methodOn(TipoHerramientaController.class).editarTipoHerramienta(id, null)).withRel("actualizar-parcial"),
            linkTo(methodOn(TipoHerramientaController.class).actualizarTipoHerramienta(id, null)).withRel("actualizar"),
            linkTo(methodOn(TipoHerramientaController.class).eliminar(id)).withRel("eliminar")
        );
    }
}
