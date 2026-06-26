package com.tool_rental.reservas.assembler;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import org.springframework.stereotype.Component;

import com.tool_rental.reservas.controller.TipoReservaController;
import com.tool_rental.reservas.model.TipoReserva;

@Component
public class TipoReservaModelAssembler implements RepresentationModelAssembler<TipoReserva, EntityModel<TipoReserva>> {

    @SuppressWarnings("null")
    @Override
    public EntityModel<TipoReserva> toModel(TipoReserva tipoReserva) {
        Integer id = tipoReserva.getIdTipoReserva();

        return EntityModel.of(tipoReserva,
                linkTo(methodOn(TipoReservaController.class).buscarTipoReserva(id)).withSelfRel(),
                linkTo(methodOn(TipoReservaController.class).listarTiposReserva()).withRel("tipos-reserva"),
                linkTo(methodOn(TipoReservaController.class).guardarTipoReserva(null)).withRel("guardar"),
                linkTo(methodOn(TipoReservaController.class).actualizarTipoReserva(id, null)).withRel("actualizar"),
                linkTo(methodOn(TipoReservaController.class).eliminarTipoReserva(id)).withRel("eliminar")
        );
    }
}



