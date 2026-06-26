package com.tool_rental.reservas.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tool_rental.reservas.model.MetodoPago;
import com.tool_rental.reservas.repository.MetodoPagoRepository;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
public class MetodoPagoService {

    @Autowired
    private MetodoPagoRepository metodoPagoRepository;

    public List<MetodoPago> obtenerTodos() {
        log.info("Obteniendo todos los metodos de pago");
        return metodoPagoRepository.findAll();
    }

    public MetodoPago buscarPorId(Integer id) {
        log.info("Buscando metodo de pago con ID {}", id);

        MetodoPago metodoPago = metodoPagoRepository.findById(id).orElse(null);

        if (metodoPago == null) {
            log.warn("No se encontro metodo de pago con ID {}", id);
        } else {
            log.info("Metodo de pago con ID {} encontrado correctamente", id);
        }

        return metodoPago;
    }

    public MetodoPago guardar(MetodoPago metodoPago) {
        log.info("Guardando metodo de pago: {}", metodoPago.getNombreMetodoPago());

        MetodoPago metodoPagoGuardado = metodoPagoRepository.save(metodoPago);

        log.info("Metodo de pago guardado correctamente con ID {}", metodoPagoGuardado.getIdMetodoPago());
        return metodoPagoGuardado;
    }

    public MetodoPago actualizar(Integer id, MetodoPago metodoPago) {
        log.info("Actualizando metodo de pago con ID {}", id);

        MetodoPago metodoPagoExistente = buscarPorId(id);

        if (metodoPagoExistente == null) {
            log.warn("No se pudo actualizar. Metodo de pago con ID {} no existe", id);
            return null;
        }
        metodoPagoExistente.setNombreMetodoPago(metodoPago.getNombreMetodoPago());

        MetodoPago metodoPagoActualizado = metodoPagoRepository.save(metodoPagoExistente);

        log.info("Metodo de pago con ID {} actualizado correctamente", id);
        return metodoPagoActualizado;
    }

    public boolean eliminar(Integer id) {
        log.info("Eliminando metodo de pago con ID {}", id);

        MetodoPago metodoPago = buscarPorId(id);

        if (metodoPago == null) {
            log.warn("No se pudo eliminar. Metodo de pago con ID {} no existe", id);
            return false;
        }
        metodoPagoRepository.delete(metodoPago);

        log.info("Metodo de pago con ID {} eliminado correctamente", id);
        return true;
    }
}


