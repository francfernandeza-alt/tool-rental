package com.toolrental.usuario.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.toolrental.usuario.dto.DireccionDTO;
import com.toolrental.usuario.model.Direccion;
import com.toolrental.usuario.repository.DireccionRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class DireccionService {

    private static final Logger logger = LoggerFactory.getLogger(DireccionService.class);

    @Autowired
    private DireccionRepository direccionRepository;

    public List<DireccionDTO> obtenerTodas() {

        logger.info("Obteniendo todas las direcciones desde la base de datos");

        return direccionRepository.findAll()
                .stream()
                .map(this::convertirADTO)
                .toList();
    }

    public DireccionDTO buscarPorId(Integer id) {

        logger.info("Buscando dirección con id {}", id);

        Direccion direccion = direccionRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Dirección con id {} no encontrada", id);
                    return new RuntimeException("Dirección no encontrada");
                });

        logger.info("Dirección encontrada correctamente");

        return convertirADTO(direccion);
    }

    public Direccion guardar(Direccion direccion) {

        logger.info("Guardando nueva dirección");

        return direccionRepository.save(direccion);
    }

    public Direccion actualizar(Integer id, Direccion direccion) {

        logger.info("Actualizando dirección con id {}", id);

        Direccion direccionEncontrada = direccionRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("No existe la dirección con id {}", id);
                    return new RuntimeException("Dirección no encontrada");
                });

        direccionEncontrada.setCalle(direccion.getCalle());
        direccionEncontrada.setNumeracion(direccion.getNumeracion());
        direccionEncontrada.setComuna(direccion.getComuna());
        direccionEncontrada.setUsuario(direccion.getUsuario());

        logger.info("Dirección actualizada correctamente");

        return direccionRepository.save(direccionEncontrada);
    }

    public String eliminar(Integer id) {

        logger.info("Eliminando dirección con id {}", id);

        Direccion direccion = direccionRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("No existe la dirección con id {}", id);
                    return new RuntimeException("Dirección no encontrada");
                });

        direccionRepository.delete(direccion);

        logger.info("Dirección eliminada correctamente");

        return "Dirección eliminada correctamente";
    }

    private DireccionDTO convertirADTO(Direccion direccion) {

        DireccionDTO dto = new DireccionDTO();

        dto.setIdDireccion(direccion.getIdDireccion());
        dto.setCalle(direccion.getCalle());
        dto.setNumeracion(direccion.getNumeracion());

        if (direccion.getComuna() != null) {
            dto.setIdComuna(direccion.getComuna().getIdComuna());
            dto.setNombreComuna(direccion.getComuna().getNombreComuna());
        }

        if (direccion.getUsuario() != null) {
            dto.setRutUsuario(direccion.getUsuario().getRutUsuario());
            dto.setNombreUsuario(direccion.getUsuario().getNombreUsuario());
        }

        return dto;
    }
}