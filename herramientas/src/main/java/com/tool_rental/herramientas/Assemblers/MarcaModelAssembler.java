package com.tool_rental.herramientas.Assemblers;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.tool_rental.herramientas.Controller.MarcaController;
import com.tool_rental.herramientas.DTO.MarcaDTO;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class MarcaModelAssembler implements RepresentationModelAssembler <MarcaDTO, EntityModel<MarcaDTO>> {
    @SuppressWarnings("null")
    @Override
    public EntityModel <MarcaDTO> toModel (MarcaDTO marcaDTO) {
        Integer id = marcaDTO.getIdMarcaDTO();
        return EntityModel.of(marcaDTO,
            linkTo(methodOn(MarcaController.class).buscarPorId(id)).withSelfRel(),
            linkTo(methodOn(MarcaController.class).todasLasMarcas()).withRel("marcas"),
            linkTo(methodOn(MarcaController.class).agregarMarca(null)).withRel("guardar"),
            linkTo(methodOn(MarcaController.class).editarMarca(id, null)).withRel("actualizar-parcial"),
            linkTo(methodOn(MarcaController.class).actualizarMarca(id, null)).withRel("actualizar"),
            linkTo(methodOn(MarcaController.class).eliminarMarca(id)).withRel("eliminar")
        );
    }
}
