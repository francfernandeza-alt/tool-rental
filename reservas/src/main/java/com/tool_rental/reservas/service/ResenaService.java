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

@Service
@Transactional
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
        List<Resena> resenas = resenaRepository.findAll();
        List<ResenaDTO> resenasDTO = new ArrayList<>();

        for (Resena resena : resenas) {
            resenasDTO.add(convertirADTO(resena));
        }

        return resenasDTO;
    }

    public ResenaDTO buscarPorId(Integer id) {
        Resena resena = resenaRepository.findById(id).orElse(null);

        if (resena == null) {
            return null;
        }

        return convertirADTO(resena);
    }

    public List<ResenaDTO> buscarPorRutUsuario(String rutUsuario) {
        List<Resena> resenas = resenaRepository.findByRutUsuario(rutUsuario);
        List<ResenaDTO> resenasDTO = new ArrayList<>();

        for (Resena resena : resenas) {
            resenasDTO.add(convertirADTO(resena));
        }

        return resenasDTO;
    }

    public List<ResenaDTO> buscarPorHerramientaId(Integer herramientaId) {
        List<Resena> resenas = resenaRepository.findByHerramientaId(herramientaId);
        List<ResenaDTO> resenasDTO = new ArrayList<>();

        for (Resena resena : resenas) {
            resenasDTO.add(convertirADTO(resena));
        }

        return resenasDTO;
    }

    public List<ResenaDTO> buscarPorReservaId(Integer reservaId) {
        List<Resena> resenas = resenaRepository.findByReservaId(reservaId);
        List<ResenaDTO> resenasDTO = new ArrayList<>();

        for (Resena resena : resenas) {
            resenasDTO.add(convertirADTO(resena));
        }

        return resenasDTO;
    }

    public ResenaDTO guardar(Resena resena) {
        if (!puntuacionValida(resena)) {
            return null;
        }

        Reserva reserva = reservaService.buscarEntidadPorId(resena.getReservaId());

        if (reserva == null) {
            return null;
        }

        if (!existeHerramienta(resena.getHerramientaId())) {
            return null;
        }

        if (resena.getFechaResena() == null) {
            resena.setFechaResena(LocalDate.now());
        }

        Resena resenaGuardada = resenaRepository.save(resena);

        return convertirADTO(resenaGuardada);
    }

    public ResenaDTO actualizar(Integer id, Resena resena) {
        Resena resenaExistente = resenaRepository.findById(id).orElse(null);

        if (resenaExistente == null) {
            return null;
        }

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
            if (!existeHerramienta(resena.getHerramientaId())) {
                return null;
            }

            resenaExistente.setHerramientaId(resena.getHerramientaId());
        }

        if (resena.getReservaId() != null) {
            Reserva reserva = reservaService.buscarEntidadPorId(resena.getReservaId());

            if (reserva == null) {
                return null;
            }

            resenaExistente.setReservaId(resena.getReservaId());
        }

        if (!puntuacionValida(resenaExistente)) {
            return null;
        }

        Resena resenaActualizada = resenaRepository.save(resenaExistente);

        return convertirADTO(resenaActualizada);
    }

    public boolean eliminar(Integer id) {
        Resena resena = resenaRepository.findById(id).orElse(null);

        if (resena == null) {
            return false;
        }

        resenaRepository.delete(resena);
        return true;
    }

    private boolean puntuacionValida(Resena resena) {
        if (resena.getPuntuacion() == null) {
            return false;
        }

        return resena.getPuntuacion() >= 1 && resena.getPuntuacion() <= 5;
    }

    private boolean existeHerramienta(Integer herramientaId) {
        try {
            webClientBuilder.build()
                    .get()
                    .uri(herramientasUrl + "/" + herramientaId)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            return true;
        } catch (Exception e) {
            return false;
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
