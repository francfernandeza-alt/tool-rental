package com.tool_rental.reservas.validaciones;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.tool_rental.reservas.model.Resena;
import com.tool_rental.reservas.service.ReservaService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ResenaValidaciones {

    @Autowired
    private ReservaService reservaService;

    @Autowired
    private WebClient.Builder webClientBuilder;

    @Value("${services.herramientas.url}")
    private String herramientasUrl;

    public void validarPuntuacion(Resena resena) {
        if (resena.getPuntuacion() == null) {
            log.error("Validacion fallida: puntuacion nula");
            throw new RuntimeException("La puntuacion de la reseña es obligatoria.");
        }

        if (resena.getPuntuacion() < 1 || resena.getPuntuacion() > 5) {
            log.error("Validacion fallida: puntuacion fuera de rango {}", resena.getPuntuacion());
            throw new RuntimeException("La puntuacion debe estar entre 1 y 5.");
        }

        log.info("Puntuacion de reseña validada correctamente");
    }

    public void validarReservaExistente(Integer reservaId) {
        if (reservaId == null) {
            log.error("Validacion fallida: reservaId nulo");
            throw new RuntimeException("El ID de la reserva es obligatorio.");
        }

        reservaService.buscarEntidadPorId(reservaId);

        log.info("Reserva {} validada correctamente", reservaId);
    }

    public void validarHerramientaExistente(Integer herramientaId) {
        if (herramientaId == null) {
            log.error("Validacion fallida: herramientaId nulo");
            throw new RuntimeException("El ID de la herramienta es obligatorio.");
        }

        try {
            log.info("Validando herramienta {} en microservicio externo", herramientaId);

            webClientBuilder.build()
                    .get()
                    .uri(herramientasUrl + "/" + herramientaId)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            log.info("Herramienta {} validada correctamente", herramientaId);

        } catch (Exception e) {
            log.error("No se pudo validar herramienta {} en servicio externo", herramientaId);
            throw new RuntimeException("La herramienta indicada no existe o el servicio de herramientas no responde.");
        }
    }
}
