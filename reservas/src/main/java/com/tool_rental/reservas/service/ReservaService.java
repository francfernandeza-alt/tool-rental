package com.tool_rental.reservas.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tool_rental.reservas.dto.ReservaDTO;
import com.tool_rental.reservas.model.MetodoPago;
import com.tool_rental.reservas.model.Reserva;
import com.tool_rental.reservas.model.TipoReserva;
import com.tool_rental.reservas.repository.ReservaRepository;
import com.tool_rental.reservas.validaciones.ReservaValidaciones;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
public class ReservaService {

    @Autowired
    private ReservaRepository reservaRepository;

    @Autowired
    private ReservaValidaciones reservaValidaciones;

    public List<ReservaDTO> obtenerTodos() {
        log.info("Obteniendo todas las reservas activas");

        List<Reserva> reservas = reservaRepository.findByActivoTrue();
        List<ReservaDTO> reservasDTO = new ArrayList<>();

        for (Reserva reserva : reservas) {
            reservasDTO.add(convertirADTO(reserva));
        }
        log.info("Total de reservas activas encontradas: {}", reservasDTO.size());
        return reservasDTO;
    }

    public ReservaDTO buscarPorId(Integer id) {
        log.info("Buscando reserva activa con ID {}", id);

        Reserva reserva = reservaRepository.findByIdReservaAndActivoTrue(id).orElseThrow(() -> {
            log.error("No se encontro reserva activa con ID {}", id);
            return new RuntimeException("No se encontro la reserva con el ID ingresado.");
        });
        return convertirADTO(reserva);
    }

    public Reserva buscarEntidadPorId(Integer id) {
        return reservaRepository.findByIdReservaAndActivoTrue(id).orElse(null);
    }

    public List<ReservaDTO> buscarPorRutUsuario(String rutUsuario) {
        log.info("Buscando reservas activas asociadas al usuario {}", rutUsuario);

        List<Reserva> reservas = reservaRepository.findByRutUsuarioAndActivoTrue(rutUsuario);
        List<ReservaDTO> reservasDTO = new ArrayList<>();

        for (Reserva reserva : reservas) {
            reservasDTO.add(convertirADTO(reserva));
        }

        log.info("Se encontraron {} reservas activas para el usuario {}", reservasDTO.size(), rutUsuario);
        return reservasDTO;
    }

    public ReservaDTO guardar(Reserva reserva) {
        log.info("Registrando nueva reserva para usuario {}", reserva.getRutUsuario());

        reservaValidaciones.validarFechas(reserva);
        reservaValidaciones.validarTipoReserva(reserva.getTipoReservaId());
        reservaValidaciones.validarMetodoPago(reserva.getMetodoPagoId());

        if (reserva.getActivo() == null) {
            reserva.setActivo(true);
        }

        Reserva reservaGuardada = reservaRepository.save(reserva);

        log.info("Reserva registrada correctamente con ID {}", reservaGuardada.getIdReserva());
        return convertirADTO(reservaGuardada);
    }

    public ReservaDTO actualizar(Integer id, Reserva reserva) {
        log.info("Actualizando reserva con ID {}", id);

        Reserva reservaExistente = reservaRepository.findByIdReservaAndActivoTrue(id).orElseThrow(() -> {
            log.error("No se encontro reserva activa con ID {}", id);
            return new RuntimeException("No se encontro la reserva con el ID ingresado.");
        });

        if (reserva.getFechaInicio() != null) {
            reservaExistente.setFechaInicio(reserva.getFechaInicio());
        }
        if (reserva.getFechaFin() != null) {
            reservaExistente.setFechaFin(reserva.getFechaFin());
        }
        if (reserva.getEstadoReserva() != null) {
            reservaExistente.setEstadoReserva(reserva.getEstadoReserva());
        }
        if (reserva.getRutUsuario() != null) {
            reservaExistente.setRutUsuario(reserva.getRutUsuario());
        }
        if (reserva.getTipoReservaId() != null) {
            reservaValidaciones.validarTipoReserva(reserva.getTipoReservaId());
            reservaExistente.setTipoReservaId(reserva.getTipoReservaId());
        }
        if (reserva.getMetodoPagoId() != null) {
            reservaValidaciones.validarMetodoPago(reserva.getMetodoPagoId());
            reservaExistente.setMetodoPagoId(reserva.getMetodoPagoId());
        }
        reservaValidaciones.validarFechas(reservaExistente);

        Reserva reservaActualizada = reservaRepository.save(reservaExistente);

        log.info("Reserva con ID {} actualizada correctamente", reservaActualizada.getIdReserva());
        return convertirADTO(reservaActualizada);
    }

    public String desactivar(Integer id) {
        log.info("Intentando desactivar reserva con ID {}", id);

        Reserva reserva = reservaRepository.findByIdReservaAndActivoTrue(id).orElseThrow(() -> {
            log.error("No se encontro reserva activa con ID {}", id);
            return new RuntimeException("No se encontro la reserva con el ID ingresado.");
        });

        reserva.setActivo(false);
        reserva.setEstadoReserva("Inactiva");
        reservaRepository.save(reserva);

        log.info("Reserva con ID {} desactivada correctamente", id);
        return "La reserva con ID " + id + " fue desactivada correctamente.";
    }


    public String eliminar(Integer id) {
        return desactivar(id);
    }

    private ReservaDTO convertirADTO(Reserva reserva) {
        ReservaDTO reservaDTO = new ReservaDTO();

        reservaDTO.setIdReserva(reserva.getIdReserva());
        reservaDTO.setFechaInicio(reserva.getFechaInicio());
        reservaDTO.setFechaFin(reserva.getFechaFin());
        reservaDTO.setEstadoReserva(reserva.getEstadoReserva());
        reservaDTO.setRutUsuario(reserva.getRutUsuario());
        reservaDTO.setTipoReservaId(reserva.getTipoReservaId());
        reservaDTO.setMetodoPagoId(reserva.getMetodoPagoId());
        reservaDTO.setActivo(reserva.getActivo());

        TipoReserva tipoReserva = reservaValidaciones.validarTipoReserva(reserva.getTipoReservaId());
        MetodoPago metodoPago = reservaValidaciones.validarMetodoPago(reserva.getMetodoPagoId());

        reservaDTO.setNombreTipoReserva(tipoReserva.getNombreTipoReserva());
        reservaDTO.setNombreMetodoPago(metodoPago.getNombreMetodoPago());

        return reservaDTO;
    }
}
