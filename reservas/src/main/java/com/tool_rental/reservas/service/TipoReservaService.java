package com.tool_rental.reservas.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tool_rental.reservas.model.TipoReserva;
import com.tool_rental.reservas.repository.TipoReservaRepository;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
public class TipoReservaService {

    @Autowired
    private TipoReservaRepository tipoReservaRepository;

    public List<TipoReserva> obtenerTodos() {
        log.info("Obteniendo todos los tipos de reserva");
        return tipoReservaRepository.findAll();
    }

    public TipoReserva buscarPorId(Integer id) {
        log.info("Buscando tipo de reserva con ID {}", id);

        TipoReserva tipoReserva = tipoReservaRepository.findById(id).orElse(null);

        if (tipoReserva == null) {
            log.warn("No se encontro tipo de reserva con ID {}", id);
        } else {
            log.info("Tipo de reserva con ID {} encontrado correctamente", id);
        }

        return tipoReserva;
    }

    public TipoReserva guardar(TipoReserva tipoReserva) {
        log.info("Guardando tipo de reserva: {}", tipoReserva.getNombreTipoReserva());

        TipoReserva tipoReservaGuardado = tipoReservaRepository.save(tipoReserva);

        log.info("Tipo de reserva guardado correctamente con ID {}", tipoReservaGuardado.getIdTipoReserva());
        return tipoReservaGuardado;
    }

    public TipoReserva actualizar(Integer id, TipoReserva tipoReserva) {
        log.info("Actualizando tipo de reserva con ID {}", id);

        TipoReserva tipoReservaExistente = buscarPorId(id);

        if (tipoReservaExistente == null) {
            log.warn("No se pudo actualizar. Tipo de reserva con ID {} no existe", id);
            return null;
        }
        tipoReservaExistente.setNombreTipoReserva(tipoReserva.getNombreTipoReserva());

        TipoReserva tipoReservaActualizado = tipoReservaRepository.save(tipoReservaExistente);

        log.info("Tipo de reserva con ID {} actualizado correctamente", id);
        return tipoReservaActualizado;
    }

    public boolean eliminar(Integer id) {
        log.info("Eliminando tipo de reserva con ID {}", id);

        TipoReserva tipoReserva = buscarPorId(id);

        if (tipoReserva == null) {
            log.warn("No se pudo eliminar. Tipo de reserva con ID {} no existe", id);
            return false;
        }
        tipoReservaRepository.delete(tipoReserva);

        log.info("Tipo de reserva con ID {} eliminado correctamente", id);
        return true;
    }
}


