package com.tool_rental.reservas.assembler;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import org.springframework.stereotype.Component;

import com.tool_rental.reservas.controller.ReservaController;
import com.tool_rental.reservas.dto.ReservaDTO;

@Component
public class ReservaModelAssembler implements RepresentationModelAssembler<ReservaDTO, EntityModel<ReservaDTO>> {

    @SuppressWarnings("null")
    @Override
    public EntityModel<ReservaDTO> toModel(ReservaDTO reservaDTO) {
        Integer id = reservaDTO.getIdReserva();

        return EntityModel.of(reservaDTO,
                linkTo(methodOn(ReservaController.class).buscarReserva(id)).withSelfRel(),
                linkTo(methodOn(ReservaController.class).listarReservas()).withRel("reservas"),
                linkTo(methodOn(ReservaController.class).guardarReserva(null)).withRel("guardar"),
                linkTo(methodOn(ReservaController.class).actualizarReserva(id, null)).withRel("actualizar"),
                linkTo(methodOn(ReservaController.class).eliminarReserva(id)).withRel("desactivar")
        );
    }
}


