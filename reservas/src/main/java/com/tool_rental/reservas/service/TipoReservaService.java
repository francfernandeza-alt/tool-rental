package com.tool_rental.reservas.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tool_rental.reservas.model.TipoReserva;
import com.tool_rental.reservas.repository.TipoReservaRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class TipoReservaService {

    @Autowired
    private TipoReservaRepository tipoReservaRepository;

    public List<TipoReserva> obtenerTodos() {
        return tipoReservaRepository.findAll();
    }

    public TipoReserva buscarPorId(Integer id) {
        return tipoReservaRepository.findById(id).orElse(null);
    }

    public TipoReserva guardar(TipoReserva tipoReserva) {
        return tipoReservaRepository.save(tipoReserva);
    }

    public TipoReserva actualizar(Integer id, TipoReserva tipoReserva) {
        TipoReserva tipoReservaExistente = buscarPorId(id);

        if (tipoReservaExistente == null) {
            return null;
        }

        tipoReservaExistente.setNombreTipoReserva(tipoReserva.getNombreTipoReserva());

        return tipoReservaRepository.save(tipoReservaExistente);
    }

    public boolean eliminar(Integer id) {
        TipoReserva tipoReserva = buscarPorId(id);

        if (tipoReserva == null) {
            return false;
        }

        tipoReservaRepository.delete(tipoReserva);
        return true;
    }
}
