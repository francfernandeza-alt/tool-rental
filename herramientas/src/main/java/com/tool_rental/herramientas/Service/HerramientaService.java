package com.tool_rental.herramientas.Service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tool_rental.herramientas.DTO.HerramientaDTO;
import com.tool_rental.herramientas.Model.Herramienta;
import com.tool_rental.herramientas.Repository.HerramientaRepository;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
public class HerramientaService {
    @Autowired
    public HerramientaRepository herramientaRepository;

    public List<HerramientaDTO> obtenerTodos() {
        log.info("Obteniendo todas las herramientas");
        try {
            return herramientaRepository.findAll().stream().map(this::convertirADTO).toList();
        } catch (Exception e) {
        log.error("Error al obtener todas las herramientas: {}", e.getMessage());
        throw e;
        }
    }

    public HerramientaDTO buscarPorId(Integer id) {
        log.info("Buscando herramienta con ID {}", id);
        Herramienta herramienta = herramientaRepository.findById(id).orElseThrow(()-> {
            log.error("Herramienta con ID {} no encontrada", id);
            return new RuntimeException("Herramienta no encontrada");
        });
        return convertirADTO(herramienta);
    }

    public String eliminar(Integer id) {
    log.info("Intentando eliminar herramienta con ID {}", id);
    try {
        Herramienta herramienta = herramientaRepository.findById(id).orElseThrow(() -> {
            log.error("Herramienta con ID {} no existe", id);
            return new RuntimeException("Herramienta con Id " + id + " no existe.");
        });
        herramientaRepository.delete(herramienta);
        log.info("Herramienta con ID {} eliminada correctamente", id);
        return "La herramienta con Id '" + herramienta.getIdHerramienta() + "' ha sido eliminada.";
    } catch (RuntimeException e) {
        log.error("Error al eliminar herramienta con ID {}: {}", id, e.getMessage());
        return e.getMessage();
        }
    }


    public Herramienta guardarHerramienta(Herramienta herramienta) {
    log.info("Guardando herramienta: {}", herramienta.getNombreHerramienta());
    try {
        validarHerramienta(herramienta);
        Herramienta guardada = herramientaRepository.save(herramienta);
        log.info("Herramienta guardada con ID {}", guardada.getIdHerramienta());
        return guardada;
    } catch (RuntimeException e) {
        log.error("Error al guardar herramienta {}: {}", herramienta.getNombreHerramienta(), e.getMessage());
        throw e;
        }
    }

    public Herramienta actualizarHerramienta(Integer id, Herramienta herramienta){
        log.info("Iniciando actualización de herramienta con ID {}", id);
        Herramienta her = herramientaRepository.findById(id)
            .orElseThrow(() -> {
                log.error("Herramienta con ID {} no existe", id);
                return new RuntimeException("Herramienta no existe");
            });
        if(herramienta.getNombreHerramienta() != null){
            her.setNombreHerramienta(herramienta.getNombreHerramienta());
            log.debug("Nombre actualizado a {}", herramienta.getNombreHerramienta());
        }
        if(herramienta.getDescripcionHerramienta() != null){
            her.setDescripcionHerramienta(herramienta.getDescripcionHerramienta());
            log.debug("Descripción actualizada");
        }
        if(herramienta.getEstadoHerramienta() != null){
            her.setEstadoHerramienta(herramienta.getEstadoHerramienta());
            log.debug("Estado actualizado a {}", herramienta.getEstadoHerramienta());
        }
        if(herramienta.getCantidadTotal() != null){
            her.setCantidadTotal(herramienta.getCantidadTotal());
            log.debug("Cantidad total actualizada a {}", herramienta.getCantidadTotal());
        }
        if(herramienta.getCantidadDisponible() != null){
            her.setCantidadDisponible(herramienta.getCantidadDisponible());
            log.debug("Cantidad disponible actualizada a {}", herramienta.getCantidadDisponible());
        }
        if(herramienta.getFechaActualizacion() != null){
            her.setFechaActualizacion(herramienta.getFechaActualizacion());
            log.debug("Fecha de actualización cambiada a {}", herramienta.getFechaActualizacion());
        }
        if(herramienta.getMarca() != null){
            her.setMarca(herramienta.getMarca());
            log.debug("Marca actualizada a {}", herramienta.getMarca().getNombreMarca());
        }
        try {
            validarHerramienta(her);
            Herramienta actualizada = herramientaRepository.save(her);
            log.info("Herramienta con ID {} actualizada correctamente", id);
            return actualizada;
        } catch (RuntimeException e) {
            log.error("Error al actualizar herramienta con ID {}: {}", id, e.getMessage());
            throw e;
        }
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

    private void validarHerramienta(Herramienta herramienta) {
    if (herramienta.getCantidadDisponible() < 0 ||
        herramienta.getCantidadDisponible() > herramienta.getCantidadTotal()) {
            log.error("Validación fallida: stock inválido para herramienta {}. Disponible={}, Total={}",
            herramienta.getNombreHerramienta(),
            herramienta.getCantidadDisponible(),
            herramienta.getCantidadTotal());
            throw new RuntimeException("Cantidad disponible inválida");
        }
    if (herramienta.getEstadoHerramienta() == null ||
        !(herramienta.getEstadoHerramienta().equals("DISPONIBLE") ||
        herramienta.getEstadoHerramienta().equals("EN_MANTENCIÓN") ||
        herramienta.getEstadoHerramienta().equals("FUERA_DE_SERVICIO"))) {
            log.error("Validación fallida: estado inválido '{}' en herramienta {}",
            herramienta.getEstadoHerramienta(),
            herramienta.getNombreHerramienta());
            throw new RuntimeException("Estado de herramienta inválido");
        }
    if (herramienta.getFechaActualizacion() != null &&
        herramienta.getFechaActualizacion().isAfter(LocalDate.now())) {
            log.error("Validación fallida: fecha futura {} en herramienta {}",
            herramienta.getFechaActualizacion(),
            herramienta.getNombreHerramienta());
            throw new RuntimeException("La fecha de actualización no puede ser futura");
        }
    log.debug("Validación exitosa para herramienta {}", herramienta.getNombreHerramienta());
    }
}
