package com.tool_rental.reservas.validaciones;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.tool_rental.reservas.model.MetodoPago;
import com.tool_rental.reservas.model.Reserva;
import com.tool_rental.reservas.model.TipoReserva;
import com.tool_rental.reservas.service.MetodoPagoService;
import com.tool_rental.reservas.service.TipoReservaService;

@ExtendWith(MockitoExtension.class)
public class ReservaValidacionesTest {

    @Mock
    private TipoReservaService tipoReservaService;

    @Mock
    private MetodoPagoService metodoPagoService;

    @InjectMocks
    private ReservaValidaciones reservaValidaciones;

    private Reserva reserva;
    private TipoReserva tipoReserva;
    private MetodoPago metodoPago;

    @BeforeEach
    void setUp() {
        reserva = new Reserva();
        reserva.setIdReserva(1);
        reserva.setFechaInicio(LocalDate.of(2026, 6, 20));
        reserva.setFechaFin(LocalDate.of(2026, 6, 22));
        reserva.setEstadoReserva("Activa");
        reserva.setRutUsuario("12345678-9");
        reserva.setTipoReservaId(1);
        reserva.setMetodoPagoId(1);

        tipoReserva = new TipoReserva();
        tipoReserva.setIdTipoReserva(1);
        tipoReserva.setNombreTipoReserva("Retiro en tienda");

        metodoPago = new MetodoPago();
        metodoPago.setIdMetodoPago(1);
        metodoPago.setNombreMetodoPago("Transferencia");
    }

    @Test
    void validarFechasNoDebeLanzarErrorSiSonValidas() {
        // Given
        reserva.setFechaInicio(LocalDate.of(2026, 6, 20));
        reserva.setFechaFin(LocalDate.of(2026, 6, 22));

        // When / Then
        assertDoesNotThrow(() -> reservaValidaciones.validarFechas(reserva));
    }

    @Test
    void validarFechasDebeLanzarErrorSiFechaInicioEsNula() {
        // Given
        reserva.setFechaInicio(null);

        // When / Then
        assertThrows(RuntimeException.class, () -> reservaValidaciones.validarFechas(reserva));
    }

    @Test
    void validarFechasDebeLanzarErrorSiFechaFinEsNula() {
        // Given
        reserva.setFechaFin(null);

        // When / Then
        assertThrows(RuntimeException.class, () -> reservaValidaciones.validarFechas(reserva));
    }

    @Test
    void validarFechasDebeLanzarErrorSiFechaFinEsAnterior() {
        // Given
        reserva.setFechaInicio(LocalDate.of(2026, 6, 20));
        reserva.setFechaFin(LocalDate.of(2026, 6, 18));

        // When / Then
        assertThrows(RuntimeException.class, () -> reservaValidaciones.validarFechas(reserva));
    }

    @Test
    void validarTipoReservaDebeRetornarTipoReservaSiExiste() {
        // Given
        when(tipoReservaService.buscarPorId(1)).thenReturn(tipoReserva);

        // When
        TipoReserva resultado = reservaValidaciones.validarTipoReserva(1);

        // Then
        assertEquals("Retiro en tienda", resultado.getNombreTipoReserva());

        verify(tipoReservaService).buscarPorId(1);
    }

    @Test
    void validarTipoReservaDebeLanzarErrorSiIdEsNulo() {
        // When / Then
        assertThrows(RuntimeException.class, () -> reservaValidaciones.validarTipoReserva(null));
    }

    @Test
    void validarTipoReservaDebeLanzarErrorSiNoExiste() {
        // Given
        when(tipoReservaService.buscarPorId(99)).thenReturn(null);

        // When / Then
        assertThrows(RuntimeException.class, () -> reservaValidaciones.validarTipoReserva(99));

        verify(tipoReservaService).buscarPorId(99);
    }

    @Test
    void validarMetodoPagoDebeRetornarMetodoPagoSiExiste() {
        // Given
        when(metodoPagoService.buscarPorId(1)).thenReturn(metodoPago);

        // When
        MetodoPago resultado = reservaValidaciones.validarMetodoPago(1);

        // Then
        assertEquals("Transferencia", resultado.getNombreMetodoPago());

        verify(metodoPagoService).buscarPorId(1);
    }

    @Test
    void validarMetodoPagoDebeLanzarErrorSiIdEsNulo() {
        // When / Then
        assertThrows(RuntimeException.class, () -> reservaValidaciones.validarMetodoPago(null));
    }

    @Test
    void validarMetodoPagoDebeLanzarErrorSiNoExiste() {
        // Given
        when(metodoPagoService.buscarPorId(99)).thenReturn(null);

        // When / Then
        assertThrows(RuntimeException.class, () -> reservaValidaciones.validarMetodoPago(99));

        verify(metodoPagoService).buscarPorId(99);
    }
}