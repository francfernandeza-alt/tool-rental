package com.tool_rental.herramientas.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tool_rental.herramientas.DTO.MarcaDTO;
import com.tool_rental.herramientas.Model.Marca;
import com.tool_rental.herramientas.Repository.MarcaRepository;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
public class MarcaService {
    @Autowired
    public MarcaRepository marcaRepository;

    public List<MarcaDTO> obtenerTodos() {
        log.info("Iniciando consulta de marcas registradas");
        try {
            List<MarcaDTO> lista = marcaRepository.findAll().stream().map(this::convertirADTO).toList();
            log.info("Se obtuvieron {} marcas exitosamente", lista.size());
            return lista;
        } catch (Exception e) {
            log.error("Error al intentar listar las marcas: {}", e.getMessage());
            throw new RuntimeException("No se pudo obtener el listado de marcas.");
        }
    }

    public MarcaDTO buscarporId(Integer id) {
        log.info("Buscando marca con ID {}", id);
        if (id == null) {
            log.error("Fallo de búsqueda: Se recibió un ID nulo.");
            throw new RuntimeException("El ID de búsqueda no puede ser nulo");
        }
        Marca marca = marcaRepository.findById(id).orElseThrow(() -> {
                log.error("Marca con ID {} no encontrada", id);
                return new RuntimeException("Marca no encontrada");
            });
        return convertirADTO(marca);
    }

    public String eliminar(Integer id) {
        log.info("Eliminando marca con ID {}", id);
        try {
            Marca marca = marcaRepository.findById(id).orElseThrow(() -> {
                    log.error("Marca con ID {} no existe", id);
                    return new RuntimeException("Marca con Id " + id + " no existe.");
            });
            marcaRepository.delete(marca);
            log.info("Marca '{}' eliminada correctamente", marca.getNombreMarca());
            return "Marca '" + marca.getNombreMarca() + "' ha sido eliminada.";
        } catch (RuntimeException e) {
            log.info("No se pudo completar la eliminación: {}", e.getMessage());
            return e.getMessage();
        } catch (Exception e) {
            log.error("Error  inesperado al eliminar ID {}: {}", id, e.getMessage());
            return "Hubo un problema al conectar con el sistema, por favor intenta más tarde.";
        }
    }

    public MarcaDTO guardarMarca (MarcaDTO marcaDTO) {
        log.info("Guardando nueva marca");
        try {
            Marca marca = new Marca();
            marca.setNombreMarca(marcaDTO.getNombreMarcaDTO());
            validarMarca(marca);
            Marca guardada = marcaRepository.save(marca);
            log.info("Marca guardada exitosamente con ID {}", guardada.getIdMarca());
            return convertirADTO(guardada);
        } catch (RuntimeException e) {
            log.error("No se pudo guardar la marca: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Error inesperado {}", e.getMessage());
            throw new RuntimeException("Hubo un problema al conectar con el sistema, por favor intenta más tarde.");
        }
    }

    public MarcaDTO actualizarMarca(Integer id, MarcaDTO marcaDTO){
        log.info("Actualizando marca con ID {}", id);
        Marca marca = marcaRepository.findById(id).orElseThrow(() ->
            new RuntimeException("Marca no existe"));
        if(marcaDTO.getNombreMarcaDTO() != null){
        marca.setNombreMarca(marcaDTO.getNombreMarcaDTO());
        }
        validarMarca(marca);
        Marca actualizada = marcaRepository.save(marca);
        log.info("Marca con ID {} actualizada correctamente", id);
        return convertirADTO(actualizada);
        
    }

    public void validarMarca(Marca marca) {
        log.info("Iniciando validación para la marca: {}", marca.getNombreMarca());
        if (marca.getNombreMarca() == null || marca.getNombreMarca().trim().isEmpty()) {
            log.error("Error: El nombre debe estar compuesto al menos de 3 caracteres.");
            throw new RuntimeException("El nombre de la marca no puede estar vacío.");
        }
        if (marca.getNombreMarca().trim().length() < 3) {
            log.error("Fallo de validación: El nombre '{}' es demasiado corto.", marca.getNombreMarca());
            throw new RuntimeException("El nombre de la marca debe tener al menos 3 caracteres.");
        }
        log.info("Validación exitosa para marca {}", marca.getNombreMarca());
    }

    public MarcaDTO convertirADTO(Marca marca) {
        if (marca == null){
            return null;
        }
        MarcaDTO dto = new MarcaDTO();
        dto.setIdMarcaDTO(marca.getIdMarca());
        dto.setNombreMarcaDTO(marca.getNombreMarca());
        return dto;
    }
}
