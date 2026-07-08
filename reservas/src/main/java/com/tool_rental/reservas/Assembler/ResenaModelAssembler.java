package com.tool_rental.reservas.assembler;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import org.springframework.stereotype.Component;

import com.tool_rental.reservas.controller.ResenaController;
import com.tool_rental.reservas.dto.ResenaDTO;

@Component
public class ResenaModelAssembler implements RepresentationModelAssembler<ResenaDTO, EntityModel<ResenaDTO>> {

    @SuppressWarnings("null")
    @Override
    public EntityModel<ResenaDTO> toModel(ResenaDTO resenaDTO) {
        Integer id = resenaDTO.getIdResena();

        return EntityModel.of(resenaDTO,
                linkTo(methodOn(ResenaController.class).buscarResena(id)).withSelfRel(),
                linkTo(methodOn(ResenaController.class).listarResenas()).withRel("resenas"),
                linkTo(methodOn(ResenaController.class).guardarResena(null)).withRel("guardar"),
                linkTo(methodOn(ResenaController.class).actualizarResena(id, null)).withRel("actualizar"),
                linkTo(methodOn(ResenaController.class).eliminarResena(id)).withRel("desactivar")
        );
    }
}