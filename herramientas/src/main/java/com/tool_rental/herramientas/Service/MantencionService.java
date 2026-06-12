package com.tool_rental.herramientas.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tool_rental.herramientas.DTO.HerramientaDTO;
import com.tool_rental.herramientas.DTO.MantencionDTO;
import com.tool_rental.herramientas.Model.Herramienta;
import com.tool_rental.herramientas.Model.Mantencion;
import com.tool_rental.herramientas.Repository.MantencionRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class MantencionService {
    @Autowired
    private MantencionRepository mantencionRepository;

    public List<MantencionDTO> findAll() {
        return mantencionRepository.findAll().stream().map(this::convertirADTO).toList();
    }

    public MantencionDTO findById(Integer id) {
        Mantencion mantencion =mantencionRepository.findById(id).orElseThrow(()-> new RuntimeException("Mantencion no encontrada"));
        return convertirADTO(mantencion);
    }

    public Mantencion save(Mantencion mantencion) {
        return mantencionRepository.save(mantencion);
    }

    public void delete(Integer id) {
        mantencionRepository.deleteById(id);
    }

    public MantencionDTO convertirADTO (Mantencion mantencion) {
        MantencionDTO dto = new MantencionDTO();
        dto.setIdMantencion(mantencion.getIdMantencion());
        dto.setFechaUltimaMantencion(mantencion.getFechaUltimaMantencion());
        dto.setVigenciaMeses(mantencion.getVigenciaMeses());
        dto.setDescripcion(mantencion.getDescripcion());
        dto.setEstado(mantencion.getEstado());

        return dto;
    }
}
