package com.tool_rental.herramientas.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tool_rental.herramientas.DTO.MaterialDTO;
import com.tool_rental.herramientas.Model.Material;
import com.tool_rental.herramientas.Repository.MaterialRepository;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
public class MaterialService {
    @Autowired
    private MaterialRepository materialRepository;

    public List<MaterialDTO> obtenerTodos() {
        log.info("Obteniendo todos los materiales");
        try{
            List<MaterialDTO> lista = materialRepository.findAll().stream().map(this::convertirADTO).toList();
            log.info("Consulta exitosa: se recuperaron {} materiales registrados", lista.size());
            return lista;
        } catch (Exception e) {
            log.error("Error inesperado al listar materiales: {}", e.getMessage());
            throw new RuntimeException("Hubo un problema al conectar con el sistema, por favor intenta más tarde.");
        }
    }

    public MaterialDTO buscarPorId(Integer id) {
        log.info("Buscando material con ID {}", id);
        if (id == null) {
            log.error("Error: no existe ID ingresado");
            throw new RuntimeException("Debes proporcionar un ID válido para realizar la búsqueda.");
        }
        Material material = materialRepository.findById(id).orElseThrow(() -> {
            log.error("Material con ID {} no encontrado", id);
            return new RuntimeException("Material no encontrado");
        });
        log.info("Material con ID {} encontrado.", id);
        return convertirADTO(material);
    }

    public MaterialDTO guardarMaterial(MaterialDTO materialDTO) {
        log.info("Guardando nuevo material");
        try {
            Material material = new Material();
            material.setNombreMaterial(materialDTO.getNombreMaterialDTO());
            validarMaterial(material);
            Material guardado = materialRepository.save(material);
            log.info("Material {} guardado exitosamente con ID {}.", guardado.getNombreMaterial(), guardado.getIdMaterial());
            return convertirADTO(guardado);
        } catch (RuntimeException e) {
            log.error("No se pudo completar el registro por regla de negocio: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Error inesperado: {}", e.getMessage());
            throw new RuntimeException("Hubo un problema al conectar con el sistema, por favor intenta más tarde.");
        }
    }

    public MaterialDTO actualizarMaterial(Integer id, MaterialDTO materialDTO) {
        log.info("Actualizando material con ID {}", id);
            Material material = materialRepository.findById(id).orElseThrow(() -> {
            log.error("Material con ID {} no existe", id);
            return new RuntimeException("Material no existe");
            });
        try{
            if (materialDTO.getNombreMaterialDTO() != null) {
                material.setNombreMaterial(materialDTO.getNombreMaterialDTO());
            }
            if (materialDTO.getDescripcionMaterialDTO() != null) {
                material.setDescripcionMaterial(materialDTO.getDescripcionMaterialDTO());
            }
            Material actualizado = materialRepository.save(material);
            log.info("Material con ID {} actualizado exitosamente.", id);
            return convertirADTO(actualizado);
        } catch (RuntimeException e) {
            log.error("Error al actualizar material: {}", e.getMessage());
            throw e;
        } catch (Exception e){
            log.error("Error inesperado al actualizar material ID {}: {}", id, e.getMessage());
            throw new RuntimeException("Hubo un problema al conectar con el sistema, por favor intenta más tarde.");
        }
    }

    public String eliminar(Integer id) {
        log.info("Eliminando material con ID {}", id);
        try {
            Material material = materialRepository.findById(id).orElseThrow(() -> {
                    log.error("Material con ID {} no existe", id);
                    return new RuntimeException("Material con Id " + id + " no existe.");
            });
            materialRepository.delete(material);
            log.info("Material '{}' eliminado correctamente", material.getNombreMaterial());
            return "Material eliminado correctamente";
        } catch (RuntimeException e) {
            log.error("Error al eliminar material con ID {}: {}", id, e.getMessage());
            return e.getMessage();
        } catch (Exception e) {
            log.error("Error inesperado al eliminar ID {}: {}", id, e.getMessage());
            return "Hubo un problema al conectar con el sistema, por favor intenta más tarde.";
        }
    }

    private void validarMaterial(Material material) {
        if (material.getNombreMaterial() == null || material.getNombreMaterial().isBlank()) {
            log.error("Validación fallida: nombre vacío en material {}", material.getIdMaterial());
            throw new RuntimeException("El nombre del material es obligatorio");
        }
        if (material.getDescripcionMaterial() == null || material.getDescripcionMaterial().isBlank()) {
            log.error("Validación fallida: descripción vacía en material {}", material.getIdMaterial());
            throw new RuntimeException("La descripción del material es obligatoria");
        }
        log.info("Validación exitosa para material {}", material.getNombreMaterial());
    }

    public MaterialDTO convertirADTO(Material material) {
        if (material == null) {
            return null;
        }
        MaterialDTO dto = new MaterialDTO();
        dto.setIdMaterialDTO(material.getIdMaterial());
        dto.setNombreMaterialDTO(material.getNombreMaterial());
        dto.setDescripcionMaterialDTO(material.getDescripcionMaterial());
        return dto;
    }
}
