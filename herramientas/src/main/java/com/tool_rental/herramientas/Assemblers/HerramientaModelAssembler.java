package com.tool_rental.herramientas.Assemblers;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.tool_rental.herramientas.Controller.HerramientaController;
import com.tool_rental.herramientas.DTO.HerramientaDTO;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class HerramientaModelAssembler implements RepresentationModelAssembler<HerramientaDTO, EntityModel<HerramientaDTO>> {
    @SuppressWarnings("null")
    @Override
    public EntityModel <HerramientaDTO> toModel(HerramientaDTO herramientaDTO) {
        Integer id = herramientaDTO.getIdHerramientaDTO();
        return EntityModel.of(herramientaDTO,
            linkTo(methodOn(HerramientaController.class).buscarPorId(id)).withSelfRel(),
            linkTo(methodOn(HerramientaController.class).todosLasHerramientas()).withRel("herramientas"),
            linkTo(methodOn(HerramientaController.class).agregarherramienta(null)).withRel("guardar"),
            linkTo(methodOn(HerramientaController.class).editarHerramienta(id, null)).withRel("actualizar-parcial"),
            linkTo(methodOn(HerramientaController.class).actualizarHerramienta(id, null)).withRel("actualizar"),
            linkTo(methodOn(HerramientaController.class).eliminarHerramienta(id)).withRel("eliminar")
        );
    }
}
