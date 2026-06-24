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
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
public class ReservaService {

    @Autowired
    private ReservaRepository reservaRepository;

    @Autowired
    private TipoReservaService tipoReservaService;

    @Autowired
    private MetodoPagoService metodoPagoService;

    public List<ReservaDTO> obtenerTodos() {
        log.info("obteniendo todas las reservas registradas");

        List<Reserva> reservas = reservaRepository.findAll();
        List<ReservaDTO> reservasDTO = new ArrayList<>();
        
        for (Reserva reserva : reservas){
            reservasDTO.add(convertirADTO(reserva));
        }
        log.info("Total de reservas encontradas : {}", reservasDTO.size());
        return reservasDTO;
    }

    public ReservaDTO buscarPorId(Integer id) {
        log.info("Buscando reserva con ID {}", id);

        Reserva reserva = reservaRepository.findById(id).orElseThrow(() -> {
            log.error("No se encontro reserva con ID {}", id);
            return new RuntimeException("No se encontro la reserva con el ID ingresado");
        });

        return convertirADTO(reserva);
    }

    public Reserva buscarEntidadPorId(Integer id) {
        log.info("Buscando entidad Reserva con ID {}", id);

        return reservaRepository.findById(id).orElseThrow(() -> {
            log.error("No se encontro la entidad Reserva con ID {}", id);
            return new RuntimeException("No se encontro la reserva asociada");
        });
    }

    public List<ReservaDTO> buscarPorRutUsuario(String rutUsuario) {
        log.info("Buscando reservas asociadas al usuario {}", rutUsuario);

        List<Reserva> reservas = reservaRepository.findByRutUsuario(rutUsuario);
        List<ReservaDTO> reservasDTO = new ArrayList<>();

        for (Reserva reserva : reservas){
            reservasDTO.add(convertirADTO(reserva));
        }

        log.info("Se encontraron {} reservas para el usuario {}", reservasDTO.size(), rutUsuario);
        return reservasDTO;
    }

    public ReservaDTO guardar(Reserva reserva) {
        log.info("Registrando nueva reserva para usuario {}", reserva.getRutUsuario());

        validarFechas(reserva);
        validarTipoReserva(reserva.getTipoReservaId());
        validarMetodoPago(reserva.getMetodoPagoId());

        try {
            Reserva reservaGuardada = reservaRepository.save(reserva);

            log.info("Reserva creada correctamente con ID {}", reservaGuardada.getIdReserva());
            return convertirADTO(reservaGuardada);

        } catch (Exception e) {
            log.error("Error al guardar reserva para usuario {}", reserva.getRutUsuario(), e);
            throw new RuntimeException("No se pudo registrar la reserva.");
        }
    }

    public ReservaDTO actualizar(Integer id, Reserva reserva) {
        log.info("Actualizando reserva con ID {}", id);

        Reserva reservaExistente = reservaRepository.findById(id).orElseThrow(() -> {
            log.error("No se encontró reserva con ID {}", id);
            return new RuntimeException("No se encontró la reserva que se desea actualizar.");
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
            validarTipoReserva(reserva.getTipoReservaId());
            reservaExistente.setTipoReservaId(reserva.getTipoReservaId());
        }
        if (reserva.getMetodoPagoId() != null) {
            validarMetodoPago(reserva.getMetodoPagoId());
            reservaExistente.setMetodoPagoId(reserva.getMetodoPagoId());
        }
        validarFechas(reservaExistente);

        try {
            Reserva reservaActualizada = reservaRepository.save(reservaExistente);

            log.info("Reserva con ID {} actualizada correctamente", reservaActualizada.getIdReserva());
            return convertirADTO(reservaActualizada);

        } catch (Exception e) {
            log.error("Error al actualizar reserva con ID {}", id, e);
            throw new RuntimeException("No se pudo actualizar la reserva.");
        }
    }

    public String eliminar(Integer id) {
        log.info("Intentando eliminar reserva con ID {}", id);

        Reserva reserva = reservaRepository.findById(id).orElseThrow(() -> {
            log.error("No se encontro reserva con ID {}", id);
            return new RuntimeException("No se encontro la reserva que desea eliminar");
        });

        reservaRepository.delete(reserva);

        log.info("Reserva con ID {} eliminada correctamente", id);
        return "La reserva con ID " + id + " fue eliminada correctamente";
    }

    private void validarFechas(Reserva reserva) {
        if (reserva.getFechaInicio() == null) {
            log.error("Validación fallida: fechaInicio nula");
            throw new RuntimeException("La fecha de inicio es obligatoria.");
        }
        if (reserva.getFechaFin() == null) {
            log.error("Validación fallida: fechaFin nula");
            throw new RuntimeException("La fecha de fin es obligatoria.");
        }
        if (reserva.getFechaFin().isBefore(reserva.getFechaInicio())) {
            log.error("Validación fallida: fechaFin {} anterior a fechaInicio {}",
                    reserva.getFechaFin(),
                    reserva.getFechaInicio());
            throw new RuntimeException("La fecha de fin no puede ser anterior a la fecha de inicio.");
        }

        log.info("Fechas de reserva validadas correctamente");
    }

    private void validarTipoReserva(Integer tipoReservaId) {
        if (tipoReservaId == null) {
            log.error("Validación fallida: tipoReservaId nulo");
            throw new RuntimeException("El tipo de reserva es obligatorio.");
        }

        TipoReserva tipoReserva = tipoReservaService.buscarPorId(tipoReservaId);

        if (tipoReserva == null) {
            log.error("No existe tipo de reserva con ID {}", tipoReservaId);
            throw new RuntimeException("El tipo de reserva indicado no existe.");
        }

        log.info("Tipo de reserva {} validado correctamente", tipoReservaId);
    }

    private void validarMetodoPago(Integer metodoPagoId) {
        if (metodoPagoId == null) {
            log.error("Validación fallida: metodoPagoId nulo");
            throw new RuntimeException("El método de pago es obligatorio.");
        }

        MetodoPago metodoPago = metodoPagoService.buscarPorId(metodoPagoId);

        if (metodoPago == null) {
            log.error("No existe método de pago con ID {}", metodoPagoId);
            throw new RuntimeException("El método de pago indicado no existe.");
        }

        log.info("Método de pago {} validado correctamente", metodoPagoId);
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
