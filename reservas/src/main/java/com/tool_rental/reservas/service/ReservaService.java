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

import jakarta.transaction.Transactional;

@Service
@Transactional
public class ReservaService {

    @Autowired
    private ReservaRepository reservaRepository;

    @Autowired
    private TipoReservaService tipoReservaService;

    @Autowired
    private MetodoPagoService metodoPagoService;

    public List<ReservaDTO> obtenerTodos() {
        List<Reserva> reservas = reservaRepository.findAll();
        List<ReservaDTO> reservasDTO = new ArrayList<>();

        for (Reserva reserva : reservas) {
            reservasDTO.add(convertirADTO(reserva));
        }

        return reservasDTO;
    }

    public ReservaDTO buscarPorId(Integer id) {
        Reserva reserva = reservaRepository.findById(id).orElse(null);

        if (reserva == null) {
            return null;
        }

        return convertirADTO(reserva);
    }

    public Reserva buscarEntidadPorId(Integer id) {
        return reservaRepository.findById(id).orElse(null);
    }

    public List<ReservaDTO> buscarPorRutUsuario(String rutUsuario) {
        List<Reserva> reservas = reservaRepository.findByRutUsuario(rutUsuario);
        List<ReservaDTO> reservasDTO = new ArrayList<>();

        for (Reserva reserva : reservas) {
            reservasDTO.add(convertirADTO(reserva));
        }

        return reservasDTO;
    }

    public ReservaDTO guardar(Reserva reserva) {
        if (!fechasValidas(reserva)) {
            return null;
        }

        TipoReserva tipoReserva = tipoReservaService.buscarPorId(reserva.getTipoReservaId());
        MetodoPago metodoPago = metodoPagoService.buscarPorId(reserva.getMetodoPagoId());

        if (tipoReserva == null || metodoPago == null) {
            return null;
        }

        Reserva reservaGuardada = reservaRepository.save(reserva);

        return convertirADTO(reservaGuardada);
    }

    public ReservaDTO actualizar(Integer id, Reserva reserva) {
        Reserva reservaExistente = reservaRepository.findById(id).orElse(null);

        if (reservaExistente == null) {
            return null;
        }

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
            TipoReserva tipoReserva = tipoReservaService.buscarPorId(reserva.getTipoReservaId());

            if (tipoReserva == null) {
                return null;
            }

            reservaExistente.setTipoReservaId(reserva.getTipoReservaId());
        }

        if (reserva.getMetodoPagoId() != null) {
            MetodoPago metodoPago = metodoPagoService.buscarPorId(reserva.getMetodoPagoId());

            if (metodoPago == null) {
                return null;
            }

            reservaExistente.setMetodoPagoId(reserva.getMetodoPagoId());
        }

        if (!fechasValidas(reservaExistente)) {
            return null;
        }

        Reserva reservaActualizada = reservaRepository.save(reservaExistente);

        return convertirADTO(reservaActualizada);
    }

    public boolean eliminar(Integer id) {
        Reserva reserva = reservaRepository.findById(id).orElse(null);

        if (reserva == null) {
            return false;
        }

        reservaRepository.delete(reserva);
        return true;
    }

    private boolean fechasValidas(Reserva reserva) {
        if (reserva.getFechaInicio() == null || reserva.getFechaFin() == null) {
            return false;
        }

        return !reserva.getFechaFin().isBefore(reserva.getFechaInicio());
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

        TipoReserva tipoReserva = tipoReservaService.buscarPorId(reserva.getTipoReservaId());
        MetodoPago metodoPago = metodoPagoService.buscarPorId(reserva.getMetodoPagoId());

        if (tipoReserva != null) {
            reservaDTO.setNombreTipoReserva(tipoReserva.getNombreTipoReserva());
        }

        if (metodoPago != null) {
            reservaDTO.setNombreMetodoPago(metodoPago.getNombreMetodoPago());
        }

        return reservaDTO;
    }
}
