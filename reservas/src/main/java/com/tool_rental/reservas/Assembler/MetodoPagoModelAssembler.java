package com.tool_rental.reservas.Assembler;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import org.springframework.stereotype.Component;

import com.tool_rental.reservas.controller.MetodoPagoController;
import com.tool_rental.reservas.model.MetodoPago;

@Component
public class MetodoPagoModelAssembler implements RepresentationModelAssembler<MetodoPago, EntityModel<MetodoPago>> {

    @SuppressWarnings("null")
    @Override
    public EntityModel<MetodoPago> toModel(MetodoPago metodoPago) {
        Integer id = metodoPago.getIdMetodoPago();

        return EntityModel.of(metodoPago,
                linkTo(methodOn(MetodoPagoController.class).buscarMetodoPago(id)).withSelfRel(),
                linkTo(methodOn(MetodoPagoController.class).listarMetodosPago()).withRel("metodos-pago"),
                linkTo(methodOn(MetodoPagoController.class).guardarMetodoPago(null)).withRel("guardar"),
                linkTo(methodOn(MetodoPagoController.class).actualizarMetodoPago(id, null)).withRel("actualizar"),
                linkTo(methodOn(MetodoPagoController.class).eliminarMetodoPago(id)).withRel("eliminar")
        );
    }
}
