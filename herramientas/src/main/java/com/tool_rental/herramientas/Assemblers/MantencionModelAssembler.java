package com.tool_rental.herramientas.Assemblers;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.tool_rental.herramientas.Controller.MantencionController;
import com.tool_rental.herramientas.DTO.MantencionDTO;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class MantencionModelAssembler implements RepresentationModelAssembler <MantencionDTO, EntityModel<MantencionDTO>> {
    @SuppressWarnings("null")
    @Override
    public EntityModel <MantencionDTO> toModel (MantencionDTO mantencionDTO) {
        Integer id = mantencionDTO.getIdMantencion();
        return EntityModel.of(mantencionDTO,
            linkTo(methodOn(MantencionController.class).buscarPorId(id)).withSelfRel(),
            linkTo(methodOn(MantencionController.class).todasLasMantenciones()).withRel("mantenciones"),
            linkTo(methodOn(MantencionController.class).agregarMantencion(null)).withRel("guardar"),
            linkTo(methodOn(MantencionController.class).editarMantencion(id, null)).withRel("actualizar-parcial"),
            linkTo(methodOn(MantencionController.class).actualizarMantencion(id, null)).withRel("actualizar"),
            linkTo(methodOn(MantencionController.class).eliminarMantencion(id)).withRel("eliminar")
        );
    }
}
