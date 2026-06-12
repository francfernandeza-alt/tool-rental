package com.tool_rental.herramientas.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tool_rental.herramientas.DTO.HerramientaDTO;
import com.tool_rental.herramientas.Model.Herramienta;
import com.tool_rental.herramientas.Repository.HerramientaRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class HerramientaService {
    @Autowired
    public HerramientaRepository herramientaRepository;

    public List<HerramientaDTO> obtenerTodos() {
        return herramientaRepository.findAll().stream().map(this::convertirADTO).toList();
    }

    public HerramientaDTO buscarPorId(Integer id) {
        Herramienta herramienta = herramientaRepository.findById(id).orElseThrow(()-> new RuntimeException("Herramienta no encontrada"));
        return convertirADTO(herramienta);
    }

    public String eliminar(Integer id) {
        try {
            Herramienta herramienta = herramientaRepository.findById(id).orElseThrow(() -> new RuntimeException("Herramienta con Id  " + id + " no existe."));
            herramientaRepository.delete(herramienta);
            return "La herramienta con Id '" + herramienta.getIdHerramienta() + " ha sido eliminada.";
        } catch (RuntimeException e) {
            return e.getMessage();
        }
    }

    public Herramienta guardarHerramienta(Herramienta herramienta) {
        return herramientaRepository.save(herramienta);
    }

    public Herramienta actualizarHerramienta(Integer id, Herramienta herramienta){
        Herramienta her = herramientaRepository.findById(id).orElseThrow(() -> new RuntimeException("Herramienta no existe"));
        if(herramienta.getIdHerramienta() != null){
            her.setIdHerramienta(herramienta.getIdHerramienta());
        }
        if(herramienta.getNombreHerramienta() != null){
            her.setNombreHerramienta(herramienta.getNombreHerramienta());
        }
        if(herramienta.getDescripcionHerramienta() != null){
            her.setDescripcionHerramienta(herramienta.getDescripcionHerramienta());
        }
        if(herramienta.getEstadoHerramienta() != null){
            her.setEstadoHerramienta(herramienta.getEstadoHerramienta());
        }
        if(herramienta.getCantidadTotal() != null){
            her.setCantidadTotal(herramienta.getCantidadTotal());
        }
        if(herramienta.getCantidadDisponible() != null){
            her.setCantidadDisponible(herramienta.getCantidadDisponible());
        }
        if(herramienta.getFechaActualizacion() != null){
            her.setFechaActualizacion(herramienta.getFechaActualizacion());
        }
        return herramientaRepository.save(her);
    }

    public HerramientaDTO convertirADTO (Herramienta herramienta) {
        HerramientaDTO dto = new HerramientaDTO();
        dto.setIdHerramientaDTO(herramienta.getIdHerramienta());
        dto.setNombreHerramientaDTO(herramienta.getNombreHerramienta());
        dto.setEstadoHerramientaDTO(herramienta.getEstadoHerramienta());
        dto.setCantidadDisponibleDtO(herramienta.getCantidadDisponible());
        dto.setNombreMarcaDTO(herramienta.getMarca().getNombreMarca());

        return dto;
    }
}
