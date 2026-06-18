package com.tool_rental.reservas.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tool_rental.reservas.model.MetodoPago;
import com.tool_rental.reservas.repository.MetodoPagoRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class MetodoPagoService {

    @Autowired
    private MetodoPagoRepository metodoPagoRepository;

    public List<MetodoPago> obtenerTodos() {
        return metodoPagoRepository.findAll();
    }

    public MetodoPago buscarPorId(Integer id) {
        return metodoPagoRepository.findById(id).orElse(null);
    }

    public MetodoPago guardar(MetodoPago metodoPago) {
        return metodoPagoRepository.save(metodoPago);
    }

    public MetodoPago actualizar(Integer id, MetodoPago metodoPago) {
        MetodoPago metodoPagoExistente = buscarPorId(id);

        if (metodoPagoExistente == null) {
            return null;
        }

        metodoPagoExistente.setNombreMetodoPago(metodoPago.getNombreMetodoPago());

        return metodoPagoRepository.save(metodoPagoExistente);
    }

    public boolean eliminar(Integer id) {
        MetodoPago metodoPago = buscarPorId(id);

        if (metodoPago == null) {
            return false;
        }

        metodoPagoRepository.delete(metodoPago);
        return true;
    }
}
