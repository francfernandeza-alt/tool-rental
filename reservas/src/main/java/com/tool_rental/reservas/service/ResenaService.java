package com.tool_rental.reservas.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tool_rental.reservas.dto.ResenaDTO;
import com.tool_rental.reservas.model.Resena;
import com.tool_rental.reservas.repository.ResenaRepository;
import com.tool_rental.reservas.validaciones.ResenaValidaciones;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
public class ResenaService {

    @Autowired
    private ResenaRepository resenaRepository;

    @Autowired
    private ResenaValidaciones resenaValidaciones;

    public List<ResenaDTO> obtenerTodos() {
        log.info("Obteniendo todas las resenas activas");

        List<Resena> resenas = resenaRepository.findByActivoTrue();
        List<ResenaDTO> resenasDTO = new ArrayList<>();

        for (Resena resena : resenas) {
            resenasDTO.add(convertirADTO(resena));
        }
        log.info("Total de resenas activas encontradas: {}", resenasDTO.size());
        return resenasDTO;
    }

    public ResenaDTO buscarPorId(Integer id) {
        log.info("Buscando resena activa con ID {}", id);

        Resena resena = resenaRepository.findByIdResenaAndActivoTrue(id).orElseThrow(() -> {
            log.error("No se encontro resena activa con ID {}", id);
            return new RuntimeException("No se encontro la resena con el ID ingresado.");
        });
        return convertirADTO(resena);
    }

    public List<ResenaDTO> buscarPorRutUsuario(String rutUsuario) {
        log.info("Buscando resenas activas asociadas al usuario {}", rutUsuario);

        List<Resena> resenas = resenaRepository.findByRutUsuarioAndActivoTrue(rutUsuario);
        List<ResenaDTO> resenasDTO = new ArrayList<>();

        for (Resena resena : resenas) {
            resenasDTO.add(convertirADTO(resena));
        }
        log.info("Se encontraron {} resenas activas para el usuario {}", resenasDTO.size(), rutUsuario);
        return resenasDTO;
    }

    public List<ResenaDTO> buscarPorHerramientaId(Integer herramientaId) {
        log.info("Buscando resenas activas asociadas a la herramienta {}", herramientaId);

        List<Resena> resenas = resenaRepository.findByHerramientaIdAndActivoTrue(herramientaId);
        List<ResenaDTO> resenasDTO = new ArrayList<>();

        for (Resena resena : resenas) {
            resenasDTO.add(convertirADTO(resena));
        }
        log.info("Se encontraron {} resenas activas para la herramienta {}", resenasDTO.size(), herramientaId);
        return resenasDTO;
    }

    public List<ResenaDTO> buscarPorReservaId(Integer reservaId) {
        log.info("Buscando resenas activas asociadas a la reserva {}", reservaId);

        List<Resena> resenas = resenaRepository.findByReservaIdAndActivoTrue(reservaId);
        List<ResenaDTO> resenasDTO = new ArrayList<>();

        for (Resena resena : resenas) {
            resenasDTO.add(convertirADTO(resena));
        }
        log.info("Se encontraron {} resenas activas para la reserva {}", resenasDTO.size(), reservaId);
        return resenasDTO;
    }

    public ResenaDTO guardar(Resena resena) {
        log.info("Registrando nueva resena para reserva {} y herramienta {}",
                resena.getReservaId(),
                resena.getHerramientaId());

        resenaValidaciones.validarPuntuacion(resena);
        resenaValidaciones.validarReservaExistente(resena.getReservaId());
        resenaValidaciones.validarHerramientaExistente(resena.getHerramientaId());

        if (resena.getFechaResena() == null) {
            resena.setFechaResena(LocalDate.now());
            log.info("Fecha de resena asignada automaticamente: {}", resena.getFechaResena());
        }

        if (resena.getActivo() == null) {
            resena.setActivo(true);
        }

        Resena resenaGuardada = resenaRepository.save(resena);

        log.info("Resena registrada correctamente con ID {}", resenaGuardada.getIdResena());
        return convertirADTO(resenaGuardada);
    }

    public ResenaDTO actualizar(Integer id, Resena resena) {
        log.info("Actualizando resena con ID {}", id);

        Resena resenaExistente = resenaRepository.findByIdResenaAndActivoTrue(id).orElseThrow(() -> {
            log.error("No se encontro resena activa con ID {}", id);
            return new RuntimeException("No se encontro la resena con el ID ingresado.");
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
            resenaValidaciones.validarHerramientaExistente(resena.getHerramientaId());
            resenaExistente.setHerramientaId(resena.getHerramientaId());
        }
        if (resena.getReservaId() != null) {
            resenaValidaciones.validarReservaExistente(resena.getReservaId());
            resenaExistente.setReservaId(resena.getReservaId());
        }
        resenaValidaciones.validarPuntuacion(resenaExistente);

        Resena resenaActualizada = resenaRepository.save(resenaExistente);

        log.info("Resena con ID {} actualizada correctamente", resenaActualizada.getIdResena());
        return convertirADTO(resenaActualizada);
    }

    public String desactivar(Integer id) {
        log.info("Intentando desactivar resena con ID {}", id);

        Resena resena = resenaRepository.findByIdResenaAndActivoTrue(id).orElseThrow(() -> {
            log.error("No se encontro resena activa con ID {}", id);
            return new RuntimeException("No se encontro la resena con el ID ingresado.");
        });

        resena.setActivo(false);
        resenaRepository.save(resena);

        log.info("Resena con ID {} desactivada correctamente", id);
        return "La resena con ID " + id + " fue desactivada correctamente.";
    }


    public String eliminar(Integer id) {
        return desactivar(id);
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
        resenaDTO.setActivo(resena.getActivo());
        return resenaDTO;
    }
}
