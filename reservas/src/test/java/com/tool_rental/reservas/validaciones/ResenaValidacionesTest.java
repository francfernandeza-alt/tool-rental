package com.tool_rental.reservas.validaciones;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.reactive.function.client.WebClient;

import com.tool_rental.reservas.model.Resena;
import com.tool_rental.reservas.model.Reserva;
import com.tool_rental.reservas.service.ReservaService;

import reactor.core.publisher.Mono;

@ExtendWith(MockitoExtension.class)
public class ResenaValidacionesTest {

    @Mock
    private ReservaService reservaService;

    @Mock
    private WebClient.Builder webClientBuilder;

    @Mock
    private WebClient webClient;

    @SuppressWarnings("rawtypes")
    @Mock
    private WebClient.RequestHeadersUriSpec requestHeadersUriSpec;

    @SuppressWarnings("rawtypes")
    @Mock
    private WebClient.RequestHeadersSpec requestHeadersSpec;

    @Mock
    private WebClient.ResponseSpec responseSpec;

    @InjectMocks
    private ResenaValidaciones resenaValidaciones;

    private Resena resena;
    private Reserva reserva;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(
                resenaValidaciones,
                "herramientasUrl",
                "http://localhost:8081/api/v1/herramientas"
        );

        resena = new Resena();
        resena.setIdResena(1);
        resena.setPuntuacion(5);
        resena.setComentario("Excelente herramienta");
        resena.setFechaResena(LocalDate.of(2026, 6, 25));
        resena.setRutUsuario("12345678-9");
        resena.setHerramientaId(1);
        resena.setReservaId(1);

        reserva = new Reserva();
        reserva.setIdReserva(1);
        reserva.setFechaInicio(LocalDate.of(2026, 6, 20));
        reserva.setFechaFin(LocalDate.of(2026, 6, 22));
        reserva.setEstadoReserva("Activa");
        reserva.setRutUsuario("12345678-9");
        reserva.setTipoReservaId(1);
        reserva.setMetodoPagoId(1);
    }

    @Test
    void validarPuntuacionNoDebeLanzarErrorSiEsValida() {
        // Given
        resena.setPuntuacion(5);

        // When / Then
        assertDoesNotThrow(() -> resenaValidaciones.validarPuntuacion(resena));
    }

    @Test
    void validarPuntuacionDebeLanzarErrorSiEsNula() {
        // Given
        resena.setPuntuacion(null);

        // When / Then
        assertThrows(RuntimeException.class, () -> resenaValidaciones.validarPuntuacion(resena));
    }

    @Test
    void validarPuntuacionDebeLanzarErrorSiEsMenorAUno() {
        // Given
        resena.setPuntuacion(0);

        // When / Then
        assertThrows(RuntimeException.class, () -> resenaValidaciones.validarPuntuacion(resena));
    }

    @Test
    void validarPuntuacionDebeLanzarErrorSiEsMayorACinco() {
        // Given
        resena.setPuntuacion(6);

        // When / Then
        assertThrows(RuntimeException.class, () -> resenaValidaciones.validarPuntuacion(resena));
    }

    @Test
    void validarReservaExistenteNoDebeLanzarErrorSiExiste() {
        // Given
        when(reservaService.buscarEntidadPorId(1)).thenReturn(reserva);

        // When / Then
        assertDoesNotThrow(() -> resenaValidaciones.validarReservaExistente(1));

        verify(reservaService).buscarEntidadPorId(1);
    }

    @Test
    void validarReservaExistenteDebeLanzarErrorSiIdEsNulo() {
        // When / Then
        assertThrows(RuntimeException.class, () -> resenaValidaciones.validarReservaExistente(null));
    }

    @Test
    void validarReservaExistenteDebeLanzarErrorSiNoExiste() {
        // Given
        when(reservaService.buscarEntidadPorId(99)).thenReturn(null);

        // When / Then
        assertThrows(RuntimeException.class, () -> resenaValidaciones.validarReservaExistente(99));

        verify(reservaService).buscarEntidadPorId(99);
    }

    @Test
    void validarHerramientaExistenteNoDebeLanzarErrorSiServicioResponde() {
        // Given
        when(webClientBuilder.build()).thenReturn(webClient);
        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri("http://localhost:8081/api/v1/herramientas/1")).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(String.class)).thenReturn(Mono.just("{}"));

        // When / Then
        assertDoesNotThrow(() -> resenaValidaciones.validarHerramientaExistente(1));

        verify(webClientBuilder).build();
        verify(webClient).get();
        verify(requestHeadersUriSpec).uri("http://localhost:8081/api/v1/herramientas/1");
        verify(requestHeadersSpec).retrieve();
        verify(responseSpec).bodyToMono(String.class);
    }

    @Test
    void validarHerramientaExistenteDebeLanzarErrorSiIdEsNulo() {
        // When / Then
        assertThrows(RuntimeException.class, () -> resenaValidaciones.validarHerramientaExistente(null));
    }

    @Test
    void validarHerramientaExistenteDebeLanzarErrorSiServicioNoResponde() {
        // Given
        when(webClientBuilder.build()).thenThrow(new RuntimeException("Servicio no disponible"));

        // When / Then
        assertThrows(RuntimeException.class, () -> resenaValidaciones.validarHerramientaExistente(99));

        verify(webClientBuilder).build();
    }
}
