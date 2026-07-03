package com.toolrental.usuario.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.toolrental.usuario.dto.ComunaDTO;
import com.toolrental.usuario.model.Comuna;
import com.toolrental.usuario.repository.ComunaRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class ComunaService {

    private static final Logger logger = LoggerFactory.getLogger(ComunaService.class);

    @Autowired
    private ComunaRepository comunaRepository;

    public List<ComunaDTO> obtenerTodas() {

        logger.info("Obteniendo todas las comunas desde la base de datos");

        return comunaRepository.findAll()
                .stream()
                .map(this::convertirADTO)
                .toList();
    }

    public ComunaDTO buscarPorId(Integer id) {

        logger.info("Buscando comuna con id {}", id);

        Comuna comuna = comunaRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Comuna con id {} no encontrada", id);
                    return new RuntimeException("Comuna no encontrada");
                });

        logger.info("Comuna encontrada correctamente");

        return convertirADTO(comuna);
    }

    public Comuna guardarComuna(Comuna comuna) {

        logger.info("Guardando nueva comuna");

        return comunaRepository.save(comuna);
    }

    public Comuna actualizarComuna(Integer id, Comuna comuna) {

        logger.info("Actualizando comuna con id {}", id);

        Comuna comunaEncontrada = comunaRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("No existe la comuna con id {}", id);
                    return new RuntimeException("Comuna no encontrada");
                });

        if (comuna.getNombreComuna() != null) {
            comunaEncontrada.setNombreComuna(comuna.getNombreComuna());
        }

        if (comuna.getRegion() != null) {
            comunaEncontrada.setRegion(comuna.getRegion());
        }

        logger.info("Comuna actualizada correctamente");

        return comunaRepository.save(comunaEncontrada);
    }

    public String eliminar(Integer id) {

        logger.info("Eliminando comuna con id {}", id);

        Comuna comuna = comunaRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("No existe la comuna con id {}", id);
                    return new RuntimeException("Comuna no encontrada");
                });

        comunaRepository.delete(comuna);

        logger.info("Comuna eliminada correctamente");

        return "Comuna eliminada correctamente";
    }

    private ComunaDTO convertirADTO(Comuna comuna) {

        ComunaDTO dto = new ComunaDTO();

        dto.setIdComuna(comuna.getIdComuna());
        dto.setNombreComuna(comuna.getNombreComuna());

        if (comuna.getRegion() != null) {
            dto.setNumeroRegion(comuna.getRegion().getNumeroRegion());
            dto.setNombreRegion(comuna.getRegion().getNombreRegion());
        }

        return dto;
    }
}