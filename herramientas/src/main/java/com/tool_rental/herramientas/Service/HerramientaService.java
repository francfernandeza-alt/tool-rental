package com.tool_rental.herramientas.Service;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.tool_rental.herramientas.DTO.HerramientaDTO;
import com.tool_rental.herramientas.DTO.ResenaDTO;
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

    @Autowired
    private WebClient.Builder webClientBuilder;

    public List<HerramientaDTO> obtenerTodos() {
        log.info("Obteniendo todas las herramientas");
        List<Herramienta> herramientas = herramientaRepository.findAll();
        return herramientas.stream().map(herramienta -> {
            HerramientaDTO dto = convertirADTO(herramienta);
            try {
                List <ResenaDTO> resenas = webClientBuilder.build().get().uri("http://localhost:8081/api/v1/herramientas/" + herramienta.getIdHerramienta())
                    .retrieve().bodyToFlux(ResenaDTO.class).collectList().block();
                if(resenas != null && !resenas.isEmpty()) {
                    dto.setTotalResenas(resenas.size());
                    double promedio = resenas.stream().mapToInt(ResenaDTO::getPuntuacion).average().orElse(0.0);
                    dto.setPromedioEvaluacion(promedio);
                    dto.setResenas(resenas);
                } else {
                    dto.setTotalResenas(0);
                    dto.setPromedioEvaluacion(0.0);
                }
            } catch (Exception e) {
                log.warn("No se pudieron cargar reseñas para la herramienta ID {}. Motivo: {}",
                        herramienta.getIdHerramienta(), e.getMessage());
                dto.setTotalResenas(0);
                dto.setPromedioEvaluacion(0.0);
            }
            return dto;
        }).toList();
    }

    public HerramientaDTO buscarPorId(Integer id) {
        log.info("Buscando herramienta con ID {}", id);
        Herramienta herramienta = herramientaRepository.findById(id).orElseThrow(()->
            new RuntimeException("No se encontró la herramienta con el ID ingresado"));
        List<ResenaDTO> resenas = Collections.emptyList();
        try {
            resenas = webClientBuilder.build().get().uri("http://localhost:8082/api/v1/resenas/herramientas/" + id)
            .retrieve().bodyToFlux(ResenaDTO.class).collectList().block();
        } catch (Exception e) {
            log.error("Fallo inesperado, reseñas no disponible momentaneamente.");
        }
        HerramientaDTO herramientaDTO = convertirADTO(herramienta);
        if (resenas != null && !resenas.isEmpty()) {
            herramientaDTO.setTotalResenas(resenas.size());
            double promedio = resenas.stream().mapToInt(ResenaDTO::getPuntuacion).average().orElse(0.0);
            herramientaDTO.setPromedioEvaluacion(promedio);
            herramientaDTO.setResenas(resenas);
            log.info("Reseñas encontradas: {} promedio de evaluaciones: {}.", resenas.size(), promedio);
        } else {
            herramientaDTO.setTotalResenas(0);
            herramientaDTO.setPromedioEvaluacion(0.0);
            log.info("La herramienta no posee reseñas registradas en el sistema externo.");
        }
        return herramientaDTO;
    }

    public String eliminar(Integer id) {
    log.info("Intentando eliminar herramienta con ID {}", id);
        Herramienta herramienta = herramientaRepository.findById(id).orElseThrow(() ->
            new RuntimeException("Herramienta con Id " + id + " no existe."));
        herramientaRepository.delete(herramienta);
        log.info("Herramienta con ID {} eliminada correctamente", id);
        return "La herramienta con Id '" + herramienta.getIdHerramienta() + "' ha sido eliminada.";
    }

    public HerramientaDTO guardarHerramienta(HerramientaDTO dto) {
    log.info("Guardando herramienta: {}", dto.getNombreHerramientaDTO());
        Herramienta herramienta = new Herramienta();
        herramienta.setNombreHerramienta(dto.getNombreHerramientaDTO());
        herramienta.setEstadoHerramienta(dto.getEstadoHerramientaDTO());
        herramienta.setCantidadDisponible(dto.getCantidadDisponibleDTO());
        herramienta.setCantidadTotal(dto.getCantidadDisponibleDTO());
        herramienta.setFechaActualizacion(LocalDate.now());
        validarHerramienta(herramienta);
        Herramienta guardada = herramientaRepository.save(herramienta);
        log.info("Herramienta guardada con ID {}", guardada.getIdHerramienta());
        return convertirADTO(guardada);
    }

    public HerramientaDTO actualizarHerramienta(Integer id, HerramientaDTO herramientaDTO){
        log.info("Iniciando actualización de herramienta con ID {}", id);
        Herramienta her = herramientaRepository.findById(id).orElseThrow(() -> {
            log.error("Herramienta con ID {} no existe", id);
            return new RuntimeException("Herramienta no existe");
        });
        if(herramientaDTO.getNombreHerramientaDTO() != null){
            her.setNombreHerramienta(herramientaDTO.getNombreHerramientaDTO());
        }
        if(herramientaDTO.getEstadoHerramientaDTO() != null){
            her.setEstadoHerramienta(herramientaDTO.getEstadoHerramientaDTO());
        }
        if(herramientaDTO.getCantidadDisponibleDTO() != null){
            her.setCantidadDisponible(herramientaDTO.getCantidadDisponibleDTO());
        }
        her.setFechaActualizacion(LocalDate.now());
        validarHerramienta(her);
        Herramienta actualizada = herramientaRepository.save(her);
        log.info("Los datos de la herramienta '{}' han sido actualizados correctamente.", actualizada.getNombreHerramienta());
        return convertirADTO(actualizada);
    }

    private HerramientaDTO convertirADTO (Herramienta herramienta) {
        if (herramienta == null ) {
            return null;
        }
        HerramientaDTO dto = new HerramientaDTO();
        dto.setIdHerramientaDTO(herramienta.getIdHerramienta());
        dto.setNombreHerramientaDTO(herramienta.getNombreHerramienta());
        dto.setEstadoHerramientaDTO(herramienta.getEstadoHerramienta());
        dto.setCantidadDisponibleDTO(herramienta.getCantidadDisponible());
        if (herramienta.getMarca() != null) {
            dto.setNombreMarcaDTO(herramienta.getMarca().getNombreMarca());
        }
        return dto;
    }

    private void validarHerramienta(Herramienta herramienta) {
    if (herramienta.getCantidadDisponible() < 0 ||
        herramienta.getCantidadTotal() < 0 ||
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
    log.info("Validación exitosa para herramienta {}", herramienta.getNombreHerramienta());
    }
}
