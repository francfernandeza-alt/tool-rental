package com.tool_rental.herramientas.Assemblers;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.tool_rental.herramientas.Controller.MaterialController;
import com.tool_rental.herramientas.DTO.MaterialDTO;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class MaterialModelAssembler implements RepresentationModelAssembler <MaterialDTO, EntityModel<MaterialDTO>> {
    @SuppressWarnings("null")
    @Override
    public EntityModel <MaterialDTO> toModel(MaterialDTO materialDTO) {
        Integer id = materialDTO.getIdMaterialDTO();
        return EntityModel.of(materialDTO,
            linkTo(methodOn(MaterialController.class).buscarPorId(id)).withSelfRel(),
            linkTo(methodOn(MaterialController.class).todosLosMateriales()).withRel("materiales"),
            linkTo(methodOn(MaterialController.class).agregarmaterial(null)).withRel("guardar"),
            linkTo(methodOn(MaterialController.class).editarMaterial(id, null)).withRel("actualizar-parcial"),
            linkTo(methodOn(MaterialController.class).actualizarMaterial(id, null)).withRel("actualizar"),
            linkTo(methodOn(MaterialController.class).eliminarMaterial(id)).withRel("eliminar")
        );
    }
    
}
