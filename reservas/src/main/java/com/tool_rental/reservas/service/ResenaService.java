package com.tool_rental.reservas.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.tool_rental.reservas.dto.ResenaDTO;
import com.tool_rental.reservas.model.Resena;
import com.tool_rental.reservas.model.Reserva;
import com.tool_rental.reservas.repository.ResenaRepository;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
public class ResenaService {

    @Autowired
    private ResenaRepository resenaRepository;

    @Autowired
    private ReservaService reservaService;

    @Autowired
    private WebClient.Builder webClientBuilder;

    @Value("${services.herramientas.url}")
    private String herramientasUrl;

    public List<ResenaDTO> obtenerTodos() {
        log.info("Obteniendo todas las reseñas registradas");

        List<Resena> resenas = resenaRepository.findAll();
        List<ResenaDTO> resenasDTO = new ArrayList<>();

        for (Resena resena : resenas) {
            resenasDTO.add(convertirADTO(resena));
        }
        log.info("Total de reseñas encontradas: {}", resenasDTO.size());
        return resenasDTO;
    }

    public ResenaDTO buscarPorId(Integer id) {
        log.info("Buscando reseña con ID {}", id);

        Resena resena = resenaRepository.findById(id).orElseThrow(() -> {
            log.error("No se encontro reseña con ID {}", id);
            return new RuntimeException("No se encontró la reseña con el ID ingresado.");
        });
        return convertirADTO(resena);
    }

    public List<ResenaDTO> buscarPorRutUsuario(String rutUsuario) {
        log.info("Buscando reseñas asociadas al usuario {}", rutUsuario);

        List<Resena> resenas = resenaRepository.findByRutUsuario(rutUsuario);
        List<ResenaDTO> resenasDTO = new ArrayList<>();

        for (Resena resena : resenas) {
            resenasDTO.add(convertirADTO(resena));
        }
        log.info("Se encontraron {} reseñas para el usuario {}", resenasDTO.size(), rutUsuario);
        return resenasDTO;
    }

    public List<ResenaDTO> buscarPorHerramientaId(Integer herramientaId) {
        log.info("Buscando reseñas asociadas a la herramienta {}", herramientaId);

        List<Resena> resenas = resenaRepository.findByHerramientaId(herramientaId);
        List<ResenaDTO> resenasDTO = new ArrayList<>();

        for (Resena resena : resenas) {
            resenasDTO.add(convertirADTO(resena));
        }
        log.info("Se encontraron {} reseñas para la herramienta {}", resenasDTO.size(), herramientaId);
        return resenasDTO;
    }

    public List<ResenaDTO> buscarPorReservaId(Integer reservaId) {
        log.info("Buscando reseñas asociadas a la reserva {}", reservaId);

        List<Resena> resenas = resenaRepository.findByReservaId(reservaId);
        List<ResenaDTO> resenasDTO = new ArrayList<>();

        for (Resena resena : resenas) {
            resenasDTO.add(convertirADTO(resena));
        }
        log.info("Se encontraron {} reseñas para la reserva {}", resenasDTO.size(), reservaId);
        return resenasDTO;
    }

    public ResenaDTO guardar(Resena resena) {
        log.info("Registrando nueva reseña para reserva {} y herramienta {}",
                resena.getReservaId(),
                resena.getHerramientaId());

        validarPuntuacion(resena);
        validarReservaExistente(resena.getReservaId());
        validarHerramientaExistente(resena.getHerramientaId());

        if (resena.getFechaResena() == null) {
            resena.setFechaResena(LocalDate.now());
            log.info("Fecha de reseña asignada automáticamente: {}", resena.getFechaResena());
        }
        Resena resenaGuardada = resenaRepository.save(resena);

        log.info("Reseña registrada correctamente con ID {}", resenaGuardada.getIdResena());
        return convertirADTO(resenaGuardada);
    }

    public ResenaDTO actualizar(Integer id, Resena resena) {
        log.info("Actualizando reseña con ID {}", id);

        Resena resenaExistente = resenaRepository.findById(id).orElseThrow(() -> {
            log.error("No se encontró reseña con ID {}", id);
            return new RuntimeException("No se encontró la reseña con el ID ingresado.");
        });

        if (resena.getPuntuacion() != null) {
            resenaExistente.setPuntuacion(resena.getPuntuacion());
        }

        if (resena.getComentario() != null) {
            resenaExistente.setComentario(resena.getComentario());
        }

        if (resena.getFechaResena() != null) {
            resenaExistente.setFechaResena(resena.getFechaResena());
        }

        if (resena.getRutUsuario() != null) {
            resenaExistente.setRutUsuario(resena.getRutUsuario());
        }

        if (resena.getHerramientaId() != null) {
            validarHerramientaExistente(resena.getHerramientaId());
            resenaExistente.setHerramientaId(resena.getHerramientaId());
        }

        if (resena.getReservaId() != null) {
            validarReservaExistente(resena.getReservaId());
            resenaExistente.setReservaId(resena.getReservaId());
        }

        validarPuntuacion(resenaExistente);

        Resena resenaActualizada = resenaRepository.save(resenaExistente);

        log.info("Reseña con ID {} actualizada correctamente", resenaActualizada.getIdResena());
        return convertirADTO(resenaActualizada);
    }

    public String eliminar(Integer id) {
        log.info("Intentando eliminar reseña con ID {}", id);

        Resena resena = resenaRepository.findById(id).orElseThrow(() -> {
            log.error("No se encontró reseña con ID {}", id);
            return new RuntimeException("No se encontró la reseña con el ID ingresado.");
        });
        resenaRepository.delete(resena);

        log.info("Reseña con ID {} eliminada correctamente", id);
        return "La reseña con ID " + id + " fue eliminada correctamente.";
    }

    private void validarPuntuacion(Resena resena) {
        if (resena.getPuntuacion() == null) {
            log.error("Validación fallida: puntuación nula");
            throw new RuntimeException("La puntuación de la reseña es obligatoria.");
        }

        if (resena.getPuntuacion() < 1 || resena.getPuntuacion() > 5) {
            log.error("Validación fallida: puntuación fuera de rango {}", resena.getPuntuacion());
            throw new RuntimeException("La puntuación debe estar entre 1 y 5.");
        }

        log.info("Validación exitosa de puntuación: {}", resena.getPuntuacion());
    }

    private void validarReservaExistente(Integer reservaId) {
        if (reservaId == null) {
            log.error("Validación fallida: reservaId nulo");
            throw new RuntimeException("El ID de la reserva es obligatorio.");
        }
        Reserva reserva = reservaService.buscarEntidadPorId(reservaId);

        if (reserva == null) {
            log.error("No existe reserva con ID {}", reservaId);
            throw new RuntimeException("La reserva indicada no existe.");
        }

        log.info("Reserva {} validada correctamente", reservaId);
    }

    private void validarHerramientaExistente(Integer herramientaId) {
        if (herramientaId == null) {
            log.error("Validación fallida: herramientaId nulo");
            throw new RuntimeException("El ID de la herramienta es obligatorio.");
        }

        try {
            log.info("Validando existencia de herramienta {} en servicio externo", herramientaId);

            webClientBuilder.build()
                    .get()
                    .uri(herramientasUrl + "/" + herramientaId)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            log.info("Herramienta {} validada correctamente desde servicio externo", herramientaId);

        } catch (Exception e) {
            log.error("Error al validar herramienta {} desde servicio externo", herramientaId);
            throw new RuntimeException("La herramienta indicada no existe o el servicio de herramientas no responde.");
        }
    }

    private ResenaDTO convertirADTO(Resena resena) {
        ResenaDTO resenaDTO = new ResenaDTO();

        resenaDTO.setIdResena(resena.getIdResena());
        resenaDTO.setPuntuacion(resena.getPuntuacion());
        resenaDTO.setComentario(resena.getComentario());
        resenaDTO.setFechaResena(resena.getFechaResena());
        resenaDTO.setRutUsuario(resena.getRutUsuario());
        resenaDTO.setHerramientaId(resena.getHerramientaId());
        resenaDTO.setReservaId(resena.getReservaId());

        return resenaDTO;
    }
}
