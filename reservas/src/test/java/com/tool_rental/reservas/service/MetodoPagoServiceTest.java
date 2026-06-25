package com.tool_rental.reservas.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import com.tool_rental.reservas.model.MetodoPago;
import com.tool_rental.reservas.repository.MetodoPagoRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class MetodoPagoServiceTest {

    @Mock
    private MetodoPagoRepository metodoPagoRepository;

    @InjectMocks
    private MetodoPagoService metodoPagoService;

    private MetodoPago metodoPago;

    @BeforeEach
    void setUp() {
        metodoPago = new MetodoPago();
        metodoPago.setIdMetodoPago(1);
        metodoPago.setNombreMetodoPago("Transferencia");
    }

    @Test
    void obtenerTodosDebeRetornarListaMetodosPago() {
        // Given
        when(metodoPagoRepository.findAll()).thenReturn(List.of(metodoPago));

        // When
        List<MetodoPago> resultado = metodoPagoService.obtenerTodos();

        // Then
        assertEquals(1, resultado.size());
        assertEquals("Transferencia", resultado.get(0).getNombreMetodoPago());

        verify(metodoPagoRepository).findAll();
    }

    @Test
    void buscarPorIdDebeRetornarMetodoPagoCuandoExiste() {
        // Given
        when(metodoPagoRepository.findById(1)).thenReturn(Optional.of(metodoPago));

        // When
        MetodoPago resultado = metodoPagoService.buscarPorId(1);

        // Then
        assertNotNull(resultado);
        assertEquals(1, resultado.getIdMetodoPago());
        assertEquals("Transferencia", resultado.getNombreMetodoPago());

        verify(metodoPagoRepository).findById(1);
    }

    @Test
    void buscarPorIdDebeRetornarNullCuandoNoExiste() {
        // Given
        when(metodoPagoRepository.findById(99)).thenReturn(Optional.empty());

        // When
        MetodoPago resultado = metodoPagoService.buscarPorId(99);

        // Then
        assertNull(resultado);

        verify(metodoPagoRepository).findById(99);
    }

    @Test
    void guardarDebeRetornarMetodoPagoGuardado() {
        // Given
        when(metodoPagoRepository.save(metodoPago)).thenReturn(metodoPago);

        // When
        MetodoPago resultado = metodoPagoService.guardar(metodoPago);

        // Then
        assertNotNull(resultado);
        assertEquals(1, resultado.getIdMetodoPago());
        assertEquals("Transferencia", resultado.getNombreMetodoPago());

        verify(metodoPagoRepository).save(metodoPago);
    }

    @Test
    void actualizarDebeRetornarMetodoPagoActualizadoCuandoExiste() {
        // Given
        MetodoPago datosActualizados = new MetodoPago();
        datosActualizados.setNombreMetodoPago("Tarjeta de débito");

        when(metodoPagoRepository.findById(1)).thenReturn(Optional.of(metodoPago));
        when(metodoPagoRepository.save(metodoPago)).thenReturn(metodoPago);

        // When
        MetodoPago resultado = metodoPagoService.actualizar(1, datosActualizados);

        // Then
        assertNotNull(resultado);
        assertEquals("Tarjeta de débito", resultado.getNombreMetodoPago());

        verify(metodoPagoRepository).findById(1);
        verify(metodoPagoRepository).save(metodoPago);
    }

    @Test
    void actualizarDebeRetornarNullCuandoNoExiste() {
        // Given
        MetodoPago datosActualizados = new MetodoPago();
        datosActualizados.setNombreMetodoPago("Tarjeta de débito");

        when(metodoPagoRepository.findById(99)).thenReturn(Optional.empty());

        // When
        MetodoPago resultado = metodoPagoService.actualizar(99, datosActualizados);

        // Then
        assertNull(resultado);

        verify(metodoPagoRepository).findById(99);
    }

    @Test
    void eliminarDebeRetornarTrueCuandoExiste() {
        // Given
        when(metodoPagoRepository.findById(1)).thenReturn(Optional.of(metodoPago));

        // When
        boolean resultado = metodoPagoService.eliminar(1);

        // Then
        assertTrue(resultado);

        verify(metodoPagoRepository).findById(1);
        verify(metodoPagoRepository).delete(metodoPago);
    }

    @Test
    void eliminarDebeRetornarFalseCuandoNoExiste() {
        // Given
        when(metodoPagoRepository.findById(99)).thenReturn(Optional.empty());

        // When
        boolean resultado = metodoPagoService.eliminar(99);

        // Then
        assertFalse(resultado);

        verify(metodoPagoRepository).findById(99);
    }
}
