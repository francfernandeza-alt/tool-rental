package com.tool_rental.herramientas.Service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tool_rental.herramientas.DTO.MantencionDTO;
import com.tool_rental.herramientas.Model.Mantencion;
import com.tool_rental.herramientas.Repository.MantencionRepository;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
public class MantencionService {
    @Autowired
    private MantencionRepository mantencionRepository;

    public List<MantencionDTO> findAll() {
        log.info("Obteniendo todas las mantenciones");
        try {
            List<MantencionDTO> lista = mantencionRepository.findAll().stream().map(this::convertirADTO).toList();
            log.info("Se registran {} registros de mantención", lista.size());
            return lista;
        } catch (Exception e) {
            log.error("Error al obtener mantenciones: {}", e.getMessage());
            throw e;
        }
    }

    public MantencionDTO findById(Integer id) {
        log.info("Iniciando búsqueda de mantención con ID: {}", id);
        if (id == null) {
            log.info("No se recibió ID.");
            throw new RuntimeException("El ID de búsqueda no puede ser nulo");
        }
        Mantencion mantencion = mantencionRepository.findById(id).orElseThrow(() -> {
            log.error("No se encontró registro de mantención para el ID {}", id);
            return new RuntimeException("Mantención con ID " + id + " no encontrada en el sistema");
        });
        log.info("Registro de mantención ID {} obtenido correctamente", id);
        return convertirADTO(mantencion);
    }

    public MantencionDTO save(MantencionDTO mantencionDTO) {
        log.info("Iniciando registro de mantención");
        try {
            Mantencion mantencion = new Mantencion();
            mantencion.setFechaUltimaMantencion(mantencionDTO.getFechaUltimaMantencion());
            mantencion.setVigenciaMeses(mantencionDTO.getVigenciaMeses());
            mantencion.setDescripcion(mantencionDTO.getDescripcion());
            mantencion.setEstado(mantencionDTO.getEstado());
            validarMantencion(mantencion);
            Mantencion guardada = mantencionRepository.save(mantencion);
            log.info("Registro de mantención guardado correctamente con ID: {}", guardada.getIdMantencion());
            return convertirADTO(guardada);
        } catch (RuntimeException e) {
            log.error("Error de validación al intentar guardar: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Error inesperado al intentar guardar regirstro de mantencion: {}", e.getMessage());
            throw new RuntimeException("Error al procesar el registro de mantención");
        }
    }

    public MantencionDTO actualizarMantencion(Integer id, MantencionDTO mantencionDTO) {
        log.info("Actualizando mantención con código: {}", id);
        Mantencion mantencion = mantencionRepository.findById(id).orElseThrow(() -> {
            log.error("No se encontró el registro de mantención con ID {}", id);
            return new RuntimeException("El registro de mantención que intenta modificar no existe.");
        });
        try {
            if (mantencionDTO.getFechaUltimaMantencion() != null) {
                mantencion.setFechaUltimaMantencion(mantencionDTO.getFechaUltimaMantencion());
            }
            if (mantencionDTO.getVigenciaMeses() != null) {
                mantencion.setVigenciaMeses(mantencionDTO.getVigenciaMeses());
            }
            if (mantencionDTO.getDescripcion() != null) {
                mantencion.setDescripcion(mantencionDTO.getDescripcion());
            }
            if (mantencionDTO.getEstado() != null) {
                mantencion.setEstado(mantencionDTO.getEstado());
            }
            validarMantencion(mantencion);
            Mantencion actualizada = mantencionRepository.save(mantencion);
            log.info("El registro de mantención ha sido actualizado exitosamente.");
            return convertirADTO(actualizada);
        } catch (RuntimeException e) {
            log.error("Error al actualizar: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Error inesperado al modificar mantención: {}", e.getMessage());
            throw new RuntimeException("Hubo un problema al conectar con el sistema, por favor intenta más tarde.");
        }
    }

    public String eliminar(Integer id) {
        log.info("Petición recibida para eliminar mantención con ID {}", id);
        try {
            Mantencion mantencion = mantencionRepository.findById(id).orElseThrow(() -> {
                log.error("Fallo de eliminación: No existe registro con ID {}", id);
                return new RuntimeException("La mantención que intenta eliminar no existe en el sistema.");
            });
            mantencionRepository.delete(mantencion);
            log.info("Mantención con ID {} eliminada correctamente", id);
            return "La mantención con Id '" + id + "' ha sido eliminada exitosamente.";
        } catch (RuntimeException e) {
            log.info("No se pudo completar la eliminación: {}", e.getMessage());
            return e.getMessage();
        } catch (Exception e) {
            log.error("Error inesperado al eliminar ID {}: {}", id, e.getMessage());
            return "Hubo un problema al conectar con el sistema, por favor intenta más tarde.";
        }
    }

    public MantencionDTO convertirADTO (Mantencion mantencion) {
        if (mantencion == null) {
            return null;
        }
        MantencionDTO dto = new MantencionDTO();
        dto.setIdMantencion(mantencion.getIdMantencion());
        dto.setFechaUltimaMantencion(mantencion.getFechaUltimaMantencion());
        dto.setVigenciaMeses(mantencion.getVigenciaMeses());
        dto.setDescripcion(mantencion.getDescripcion());
        dto.setEstado(mantencion.getEstado());

        return dto;
    }

    public void validarMantencion(Mantencion mantencion) {
        if (mantencion.getFechaUltimaMantencion() != null &&
            mantencion.getFechaUltimaMantencion().isAfter(LocalDate.now())) {
                log.error("Validación fallida: fecha futura {} en mantención {}",
                mantencion.getFechaUltimaMantencion(), mantencion.getDescripcion());
                throw new RuntimeException("La fecha de mantención no puede ser futura");
        }
        if (mantencion.getVigenciaMeses() == null || mantencion.getVigenciaMeses() <= 0) {
            log.error("Validación fallida: vigencia inválida {} meses en mantención {}",
            mantencion.getVigenciaMeses(), mantencion.getDescripcion());
            throw new RuntimeException("La vigencia debe ser mayor a 0 meses");
        }
        if (mantencion.getEstado() == null ||
            !(mantencion.getEstado().equals("VIGENTE") ||
            mantencion.getEstado().equals("EXPIRADA") ||
            mantencion.getEstado().equals("PROGRAMADA"))) {
                log.error("Validación fallida: estado inválido '{}' en mantención {}",
                mantencion.getEstado(), mantencion.getDescripcion());
                throw new RuntimeException("Estado de mantención inválido");
        }
        log.info("Validación exitosa para mantención {}", mantencion.getDescripcion());
    }
}
