package com.tool_rental.herramientas.Service;

import java.util.List;

import javax.management.RuntimeErrorException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tool_rental.herramientas.DTO.MaterialDTO;
import com.tool_rental.herramientas.DTO.TipoHerramientaDTO;
import com.tool_rental.herramientas.Model.Material;
import com.tool_rental.herramientas.Model.TipoHerramienta;
import com.tool_rental.herramientas.Repository.TipoHerramientaRepository;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
public class TipoHerramientaService {
    @Autowired
    private TipoHerramientaRepository tipoHerramientaRepository;

    public List<TipoHerramientaDTO> findAll() {
        log.info("Obteniendo todos los tipos de herramienta");
        try {
            List<TipoHerramientaDTO> lista = tipoHerramientaRepository.findAll().stream().map(this::convertirADTO).toList();
            log.info("Se registran {} tipos de herramienta", lista.size());
            return lista;
        } catch (Exception e) {
            log.error("Error al obtener tipos de herramienta: {}", e.getMessage());
            throw new RuntimeException("Problemas en el sistema, intente más tarde");
        }
    }

    public TipoHerramientaDTO findById(Integer id) {
        log.info("Buscando tipo de herramienta con ID {}", id);
        if (id == null) {
            log.error("No se recibió ID para búsqueda");
            throw new RuntimeException("El ID de búsqueda no puede ser nulo");
        }
        try{
            TipoHerramienta tipo = tipoHerramientaRepository.findById(id).orElseThrow(() -> {
                log.error("No se encontró tipo de herramienta con ID {}", id);
                return new RuntimeException("Tipo de herramienta no encontrado");
            });
        log.info("Tipo de herramienta con ID {} obtenido correctamente", id);
        return convertirADTO(tipo);
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error  inesperado al buscar ID {}: {}", id, e.getMessage());
            throw new RuntimeException("Hubo un problema al conectar con el sistema, por favor intenta más tarde.");
        }
    }

    public TipoHerramientaDTO save(TipoHerramientaDTO tipoHerramientaDTO) {
        log.info("Guardando nuevo tipo de herramienta");
        try {
            TipoHerramienta tipoHerramienta = new TipoHerramienta();
            tipoHerramienta.setNombreTipoHerramienta(tipoHerramientaDTO.getNombreTipoHerramientaDTO());
            tipoHerramienta.setDescripcionTipoHerramienta(tipoHerramientaDTO.getDescripcionTipoHerramientaDTO());
            validarTipoHerramienta(tipoHerramienta);
            TipoHerramienta guardada = tipoHerramientaRepository.save(tipoHerramienta);
            log.info("Tipo de herramienta guardado con ID {}", guardada.getIdTipoHerramienta());
            return convertirADTO(guardada);
        } catch (RuntimeException e) {
            log.error("Error de validación al guardar tipo de herramienta: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Error crítico al guardar tipo de herramienta: {}", e.getMessage());
            throw new RuntimeException("Error al procesar el registro de tipo de herramienta");
        }
    }

    public TipoHerramientaDTO actualizarTipoHerramienta(Integer id, TipoHerramientaDTO tipoHerramientaDTO) {
        log.info("Actualizando tipo de herramienta.");
        TipoHerramienta tipoHerramienta = tipoHerramientaRepository.findById(id).orElseThrow(() ->
            new RuntimeException("El tipo de herramienta no existe."));
        tipoHerramienta.setNombreTipoHerramienta(tipoHerramientaDTO.getNombreTipoHerramientaDTO());
        tipoHerramienta.setDescripcionTipoHerramienta(tipoHerramientaDTO.getDescripcionTipoHerramientaDTO());
        validarTipoHerramienta(tipoHerramienta);
        TipoHerramienta actualizada = tipoHerramientaRepository.save(tipoHerramienta);
        log.info("Actualizado tipo de herramienta con ID {}.", id);
        return convertirADTO(actualizada);
    }

    public String eliminar(Integer id) {
        log.info("Eliminando tipo de herramienta con ID {}", id);
        try {
            TipoHerramienta tipo = tipoHerramientaRepository.findById(id).orElseThrow(() -> {
                log.error("No existe tipo de herramienta con ID {}", id);
                return new RuntimeException("El tipo de herramienta no existe");
                });
            tipoHerramientaRepository.delete(tipo);
            log.info("Tipo de herramienta con ID {} eliminado correctamente", id);
            return "El tipo de herramienta con Id " + id + " ha sido eliminado exitosamente.";
        } catch (RuntimeException e) {
            log.error("Error al eliminar tipo de herramienta con ID {}: {}", id, e.getMessage());
            return e.getMessage();
        } catch (Exception e) {
            log.error("Error inesperado al eliminar tipo de herramienta con ID {}: {}", id, e.getMessage());
            return "Hubo un problema al conectar con el sistema, por favor intenta más tarde.";
        }
    }

    public TipoHerramientaDTO convertirADTO(TipoHerramienta tipoHerramienta) {
        if (tipoHerramienta == null) {
            return null;
        }
        TipoHerramientaDTO dto = new TipoHerramientaDTO();
        dto.setIdTipoHerramientaDTO(tipoHerramienta.getIdTipoHerramienta());
        dto.setNombreTipoHerramientaDTO(tipoHerramienta.getNombreTipoHerramienta());
        dto.setDescripcionTipoHerramientaDTO(tipoHerramienta.getDescripcionTipoHerramienta());
        return dto;
    }

    public void validarTipoHerramienta(TipoHerramienta tipoHerramienta) {
        log.info("Iniciando validación para el tipo de herramienta: {}", tipoHerramienta.getNombreTipoHerramienta());
        if (tipoHerramienta.getNombreTipoHerramienta() == null || tipoHerramienta.getNombreTipoHerramienta().isBlank()) {
            log.error("Validación fallida: nombre vacío en tipo de herramienta {}", tipoHerramienta.getIdTipoHerramienta());
            throw new RuntimeException("El nombre del tipo de herramienta es obligatorio");
        }
        if (tipoHerramienta.getDescripcionTipoHerramienta() != null) {
            tipoHerramienta.setDescripcionTipoHerramienta(tipoHerramienta.getDescripcionTipoHerramienta().trim());
            if (tipoHerramienta.getDescripcionTipoHerramienta().length() > 255) {
                log.error("Fallo de validación: La descripción excede los 255 caracteres.");
                throw new RuntimeException("La descripción es opcional, pero no puede superar los 255 caracteres.");
            }
        }
        log.info("Validación exitosa para tipo de herramienta {}", tipoHerramienta.getNombreTipoHerramienta());
    }
}
