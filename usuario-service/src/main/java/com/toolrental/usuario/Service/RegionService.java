package com.toolrental.usuario.service;

import java.util.List;

import jakarta.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.toolrental.usuario.dto.RegionDTO;
import com.toolrental.usuario.model.Region;
import com.toolrental.usuario.repository.RegionRepository;

@Service
@Transactional
public class RegionService {

    private static final Logger logger = LoggerFactory.getLogger(RegionService.class);

    @Autowired
    private RegionRepository regionRepository;

    public List<RegionDTO> obtenerTodas() {

        logger.info("Obteniendo todas las regiones");

        return regionRepository.findAll()
                .stream()
                .map(this::convertirADTO)
                .toList();
    }

    public RegionDTO buscarPorId(Integer id) {

        logger.info("Buscando región con id {}", id);

        Region region = regionRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Región con id {} no encontrada", id);
                    return new RuntimeException("Región no encontrada");
                });

        logger.info("Región encontrada correctamente");

        return convertirADTO(region);
    }

    public Region guardar(Region region) {

        logger.info("Guardando nueva región");

        return regionRepository.save(region);
    }

    public Region actualizar(Integer id, Region region) {

        logger.info("Actualizando región con id {}", id);

        Region regionEncontrada = regionRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Región con id {} no encontrada", id);
                    return new RuntimeException("Región no encontrada");
                });

        if (region.getNombreRegion() != null) {
            regionEncontrada.setNombreRegion(region.getNombreRegion());
        }

        logger.info("Región actualizada correctamente");

        return regionRepository.save(regionEncontrada);
    }

    public String eliminar(Integer id) {

        logger.info("Eliminando región con id {}", id);

        Region region = regionRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Región con id {} no encontrada", id);
                    return new RuntimeException("Región no encontrada");
                });

        regionRepository.delete(region);

        logger.info("Región eliminada correctamente");

        return "Región eliminada correctamente";
    }

    private RegionDTO convertirADTO(Region region) {

        RegionDTO dto = new RegionDTO();

        dto.setNumeroRegion(region.getNumeroRegion());
        dto.setNombreRegion(region.getNombreRegion());

        return dto;
    }
}