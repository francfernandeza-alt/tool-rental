package com.tool_rental.reservas.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.tool_rental.reservas.dto.ReservaDTO;
import com.tool_rental.reservas.model.MetodoPago;
import com.tool_rental.reservas.model.Reserva;
import com.tool_rental.reservas.model.TipoReserva;
import com.tool_rental.reservas.repository.ReservaRepository;
import com.tool_rental.reservas.validaciones.ReservaValidaciones;

@ExtendWith(MockitoExtension.class)
public class ReservaServiceTest {

    @Mock
    private ReservaRepository reservaRepository;

    @Mock
    private ReservaValidaciones reservaValidaciones;

    @InjectMocks
    private ReservaService reservaService;

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
    void obtenerTodosDebeRetornarListaReservasDTO() {
        // Given
        when(reservaRepository.findByActivoTrue()).thenReturn(List.of(reserva));
        when(reservaValidaciones.validarTipoReserva(1)).thenReturn(tipoReserva);
        when(reservaValidaciones.validarMetodoPago(1)).thenReturn(metodoPago);

        // When
        List<ReservaDTO> resultado = reservaService.obtenerTodos();

        // Then
        assertEquals(1, resultado.size());
        assertEquals(1, resultado.get(0).getIdReserva());
        assertEquals("Activa", resultado.get(0).getEstadoReserva());
        assertEquals("Retiro en tienda", resultado.get(0).getNombreTipoReserva());
        assertEquals("Transferencia", resultado.get(0).getNombreMetodoPago());

        verify(reservaRepository).findByActivoTrue();
    }

    @Test
    void buscarPorIdDebeRetornarReservaDTOCuandoExiste() {
        // Given
        when(reservaRepository.findByIdReservaAndActivoTrue(1)).thenReturn(Optional.of(reserva));
        when(reservaValidaciones.validarTipoReserva(1)).thenReturn(tipoReserva);
        when(reservaValidaciones.validarMetodoPago(1)).thenReturn(metodoPago);

        // When
        ReservaDTO resultado = reservaService.buscarPorId(1);

        // Then
        assertNotNull(resultado);
        assertEquals(1, resultado.getIdReserva());
        assertEquals("Activa", resultado.getEstadoReserva());
        assertEquals("Retiro en tienda", resultado.getNombreTipoReserva());
        assertEquals("Transferencia", resultado.getNombreMetodoPago());

        verify(reservaRepository).findByIdReservaAndActivoTrue(1);
    }

    @Test
    void buscarPorIdDebeLanzarErrorCuandoNoExiste() {
        // Given
        when(reservaRepository.findByIdReservaAndActivoTrue(99)).thenReturn(Optional.empty());

        // When / Then
        assertThrows(RuntimeException.class, () -> reservaService.buscarPorId(99));

        verify(reservaRepository).findByIdReservaAndActivoTrue(99);
    }

    @Test
    void buscarEntidadPorIdDebeRetornarReservaCuandoExiste() {
        // Given
        when(reservaRepository.findByIdReservaAndActivoTrue(1)).thenReturn(Optional.of(reserva));

        // When
        Reserva resultado = reservaService.buscarEntidadPorId(1);

        // Then
        assertNotNull(resultado);
        assertEquals(1, resultado.getIdReserva());

        verify(reservaRepository).findByIdReservaAndActivoTrue(1);
    }

    @Test
    void buscarEntidadPorIdDebeRetornarNullCuandoNoExiste() {
        // Given
        when(reservaRepository.findByIdReservaAndActivoTrue(99)).thenReturn(Optional.empty());

        // When
        Reserva resultado = reservaService.buscarEntidadPorId(99);

        // Then
        assertNull(resultado);

        verify(reservaRepository).findByIdReservaAndActivoTrue(99);
    }

    @Test
    void buscarPorRutUsuarioDebeRetornarReservasDelUsuario() {
        // Given
        when(reservaRepository.findByRutUsuarioAndActivoTrue("12345678-9")).thenReturn(List.of(reserva));
        when(reservaValidaciones.validarTipoReserva(1)).thenReturn(tipoReserva);
        when(reservaValidaciones.validarMetodoPago(1)).thenReturn(metodoPago);

        // When
        List<ReservaDTO> resultado = reservaService.buscarPorRutUsuario("12345678-9");

        // Then
        assertEquals(1, resultado.size());
        assertEquals("12345678-9", resultado.get(0).getRutUsuario());

        verify(reservaRepository).findByRutUsuarioAndActivoTrue("12345678-9");
    }

    @Test
    void guardarDebeCrearReservaCuandoDatosSonValidos() {
        // Given
        doNothing().when(reservaValidaciones).validarFechas(reserva);
        when(reservaValidaciones.validarTipoReserva(1)).thenReturn(tipoReserva);
        when(reservaValidaciones.validarMetodoPago(1)).thenReturn(metodoPago);
        when(reservaRepository.save(reserva)).thenReturn(reserva);

        // When
        ReservaDTO resultado = reservaService.guardar(reserva);

        // Then
        assertNotNull(resultado);
        assertEquals(1, resultado.getIdReserva());
        assertEquals("Activa", resultado.getEstadoReserva());
        assertEquals("Retiro en tienda", resultado.getNombreTipoReserva());
        assertEquals("Transferencia", resultado.getNombreMetodoPago());

        verify(reservaValidaciones).validarFechas(reserva);
        verify(reservaRepository).save(reserva);
    }

    @Test
    void actualizarDebeModificarReservaCuandoExiste() {
        // Given
        Reserva datosActualizados = new Reserva();
        datosActualizados.setFechaInicio(LocalDate.of(2026, 6, 21));
        datosActualizados.setFechaFin(LocalDate.of(2026, 6, 23));
        datosActualizados.setEstadoReserva("Finalizada");
        datosActualizados.setRutUsuario("12345678-9");
        datosActualizados.setTipoReservaId(1);
        datosActualizados.setMetodoPagoId(1);

        when(reservaRepository.findByIdReservaAndActivoTrue(1)).thenReturn(Optional.of(reserva));

        when(reservaValidaciones.validarTipoReserva(1)).thenReturn(tipoReserva);
        when(reservaValidaciones.validarMetodoPago(1)).thenReturn(metodoPago);
        doNothing().when(reservaValidaciones).validarFechas(reserva);

        when(reservaRepository.save(reserva)).thenReturn(reserva);

        // When
        ReservaDTO resultado = reservaService.actualizar(1, datosActualizados);

        // Then
        assertNotNull(resultado);
        assertEquals("Finalizada", resultado.getEstadoReserva());
        assertEquals(LocalDate.of(2026, 6, 21), resultado.getFechaInicio());
        assertEquals(LocalDate.of(2026, 6, 23), resultado.getFechaFin());

        verify(reservaRepository).findByIdReservaAndActivoTrue(1);
        verify(reservaRepository).save(reserva);
    }

    @Test
    void actualizarDebeLanzarErrorCuandoNoExiste() {
        // Given
        Reserva datosActualizados = new Reserva();
        datosActualizados.setEstadoReserva("Finalizada");

        when(reservaRepository.findByIdReservaAndActivoTrue(99)).thenReturn(Optional.empty());

        // When / Then
        assertThrows(RuntimeException.class, () -> reservaService.actualizar(99, datosActualizados));

        verify(reservaRepository).findByIdReservaAndActivoTrue(99);
    }

    @Test
    void eliminarDebeRetornarMensajeCuandoExiste() {
        // Given
        when(reservaRepository.findByIdReservaAndActivoTrue(1)).thenReturn(Optional.of(reserva));

        // When
        String resultado = reservaService.eliminar(1);

        // Then
        assertEquals("La reserva con ID 1 fue desactivada correctamente.", resultado);

        verify(reservaRepository).findByIdReservaAndActivoTrue(1);
        verify(reservaRepository).save(reserva);
    }

    @Test
    void eliminarDebeLanzarErrorCuandoNoExiste() {
        // Given
        when(reservaRepository.findByIdReservaAndActivoTrue(99)).thenReturn(Optional.empty());

        // When / Then
        assertThrows(RuntimeException.class, () -> reservaService.eliminar(99));

        verify(reservaRepository).findByIdReservaAndActivoTrue(99);
    }
}