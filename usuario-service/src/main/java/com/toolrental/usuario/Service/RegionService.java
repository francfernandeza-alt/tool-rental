package com.toolrental.usuario.Service;
import java.util.List;
import jakarta.transaction.Transactional;

import com.toolrental.usuario.Model.Region;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.toolrental.usuario.Dto.RegionDTO;
import com.toolrental.usuario.Repository.RegionRepository;

@Service
@Transactional
public class RegionService {
 @Autowired
    private RegionRepository regionRepository;

    public List<RegionDTO> obtenerTodas() {
        return regionRepository.findAll()
                .stream()
                .map(this::convertirADTO)
                .toList();
    }

    public RegionDTO buscarPorId(Integer id) {

        Region region = regionRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Región no encontrada"));

        return convertirADTO(region);
    }

    public Region guardar(Region region) {
        return regionRepository.save(region);
    }

    public Region actualizar(Integer id, Region region) {

        Region regionEncontrada = regionRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Región no encontrada"));

        if (region.getNombreRegion() != null) {
            regionEncontrada.setNombreRegion(
                    region.getNombreRegion());
        }

        return regionRepository.save(regionEncontrada);
    }

    public String eliminar(Integer id) {

        Region region = regionRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Región no encontrada"));

        regionRepository.delete(region);

        return "Región eliminada correctamente";
    }

    private RegionDTO convertirADTO(Region region) {

        RegionDTO dto = new RegionDTO();

        dto.setNumeroRegion(region.getNumeroRegion());
        dto.setNombreRegion(region.getNombreRegion());

        return dto;
    }
}
