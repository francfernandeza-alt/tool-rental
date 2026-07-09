package com.tool_rental.reservas.validaciones;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tool_rental.reservas.model.MetodoPago;
import com.tool_rental.reservas.model.Reserva;
import com.tool_rental.reservas.model.TipoReserva;
import com.tool_rental.reservas.service.MetodoPagoService;
import com.tool_rental.reservas.service.TipoReservaService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ReservaValidaciones {

    @Autowired
    private TipoReservaService tipoReservaService;

    @Autowired
    private MetodoPagoService metodoPagoService;

    public void validarFechas(Reserva reserva) {
        if (reserva.getFechaInicio() == null) {
            log.error("Validacion fallida: fecha de inicio nula");
            throw new RuntimeException("La fecha de inicio es obligatoria.");
        }

        if (reserva.getFechaFin() == null) {
            log.error("Validacion fallida: fecha de fin nula");
            throw new RuntimeException("La fecha de fin es obligatoria.");
        }

        if (reserva.getFechaFin().isBefore(reserva.getFechaInicio())) {
            log.error("Validacion fallida: fecha de fin anterior a fecha de inicio");
            throw new RuntimeException("La fecha de fin no puede ser anterior a la fecha de inicio.");
        }

        log.info("Fechas de reserva validadas correctamente");
    }

    public TipoReserva validarTipoReserva(Integer tipoReservaId) {
        if (tipoReservaId == null) {
            log.error("Validacion fallida: tipoReservaId nulo");
            throw new RuntimeException("El tipo de reserva es obligatorio.");
        }

        TipoReserva tipoReserva = tipoReservaService.buscarPorId(tipoReservaId);

        if (tipoReserva == null) {
            log.error("No existe tipo de reserva con ID {}", tipoReservaId);
            throw new RuntimeException("No existe el tipo de reserva indicado.");
        }

        log.info("Tipo de reserva {} validado correctamente", tipoReservaId);
        return tipoReserva;
    }

    public MetodoPago validarMetodoPago(Integer metodoPagoId) {
        if (metodoPagoId == null) {
            log.error("Validacion fallida: metodoPagoId nulo");
            throw new RuntimeException("El metodo de pago es obligatorio.");
        }

        MetodoPago metodoPago = metodoPagoService.buscarPorId(metodoPagoId);

        if (metodoPago == null) {
            log.error("No existe metodo de pago con ID {}", metodoPagoId);
            throw new RuntimeException("No existe el metodo de pago indicado.");
        }

        log.info("Metodo de pago {} validado correctamente", metodoPagoId);
        return metodoPago;
    }
}