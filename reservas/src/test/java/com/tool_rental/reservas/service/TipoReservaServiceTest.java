package com.tool_rental.reservas.service;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.tool_rental.reservas.model.TipoReserva;
import com.tool_rental.reservas.repository.TipoReservaRepository;

@ExtendWith(MockitoExtension.class)
public class TipoReservaServiceTest {

    @Mock
    private TipoReservaRepository tipoReservaRepository;

    @InjectMocks
    private TipoReservaService tipoReservaService;

    private TipoReserva tipoReserva;

    @BeforeEach
    void setUp() {
        tipoReserva = new TipoReserva();
        tipoReserva.setIdTipoReserva(1);
        tipoReserva.setNombreTipoReserva("Retiro en tienda");
    }

    @Test
    void obtenerTodosDebeRetornarListaTiposReserva() {
        // Given
        when(tipoReservaRepository.findAll()).thenReturn(List.of(tipoReserva));

        // When
        List<TipoReserva> resultado = tipoReservaService.obtenerTodos();

        // Then
        assertEquals(1, resultado.size());
        assertEquals("Retiro en tienda", resultado.get(0).getNombreTipoReserva());

        verify(tipoReservaRepository).findAll();
    }

    @Test
    void buscarPorIdDebeRetornarTipoReservaCuandoExiste() {
        // Given
        when(tipoReservaRepository.findById(1)).thenReturn(Optional.of(tipoReserva));

        // When
        TipoReserva resultado = tipoReservaService.buscarPorId(1);

        // Then
        assertNotNull(resultado);
        assertEquals(1, resultado.getIdTipoReserva());
        assertEquals("Retiro en tienda", resultado.getNombreTipoReserva());

        verify(tipoReservaRepository).findById(1);
    }

    @Test
    void buscarPorIdDebeRetornarNullCuandoNoExiste() {
        // Given
        when(tipoReservaRepository.findById(99)).thenReturn(Optional.empty());

        // When
        TipoReserva resultado = tipoReservaService.buscarPorId(99);

        // Then
        assertNull(resultado);

        verify(tipoReservaRepository).findById(99);
    }

    @Test
    void guardarDebeRetornarTipoReservaGuardado() {
        // Given
        when(tipoReservaRepository.save(tipoReserva)).thenReturn(tipoReserva);

        // When
        TipoReserva resultado = tipoReservaService.guardar(tipoReserva);

        // Then
        assertNotNull(resultado);
        assertEquals(1, resultado.getIdTipoReserva());
        assertEquals("Retiro en tienda", resultado.getNombreTipoReserva());

        verify(tipoReservaRepository).save(tipoReserva);
    }

    @Test
    void actualizarDebeRetornarTipoReservaActualizadoCuandoExiste() {
        // Given
        TipoReserva datosActualizados = new TipoReserva();
        datosActualizados.setNombreTipoReserva("Despacho a domicilio");

        when(tipoReservaRepository.findById(1)).thenReturn(Optional.of(tipoReserva));
        when(tipoReservaRepository.save(tipoReserva)).thenReturn(tipoReserva);

        // When
        TipoReserva resultado = tipoReservaService.actualizar(1, datosActualizados);

        // Then
        assertNotNull(resultado);
        assertEquals("Despacho a domicilio", resultado.getNombreTipoReserva());

        verify(tipoReservaRepository).findById(1);
        verify(tipoReservaRepository).save(tipoReserva);
    }

    @Test
    void actualizarDebeRetornarNullCuandoNoExiste() {
        // Given
        TipoReserva datosActualizados = new TipoReserva();
        datosActualizados.setNombreTipoReserva("Despacho a domicilio");

        when(tipoReservaRepository.findById(99)).thenReturn(Optional.empty());

        // When
        TipoReserva resultado = tipoReservaService.actualizar(99, datosActualizados);

        // Then
        assertNull(resultado);

        verify(tipoReservaRepository).findById(99);
    }

    @Test
    void eliminarDebeRetornarTrueCuandoExiste() {
        // Given
        when(tipoReservaRepository.findById(1)).thenReturn(Optional.of(tipoReserva));

        // When
        boolean resultado = tipoReservaService.eliminar(1);

        // Then
        assertTrue(resultado);

        verify(tipoReservaRepository).findById(1);
        verify(tipoReservaRepository).delete(tipoReserva);
    }

    @Test
    void eliminarDebeRetornarFalseCuandoNoExiste() {
        // Given
        when(tipoReservaRepository.findById(99)).thenReturn(Optional.empty());

        // When
        boolean resultado = tipoReservaService.eliminar(99);

        // Then
        assertFalse(resultado);

        verify(tipoReservaRepository).findById(99);
    }
}


