package com.tool_rental.reservas.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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

import com.tool_rental.reservas.dto.ResenaDTO;
import com.tool_rental.reservas.model.Resena;
import com.tool_rental.reservas.repository.ResenaRepository;
import com.tool_rental.reservas.validaciones.ResenaValidaciones;

@ExtendWith(MockitoExtension.class)
public class ResenaServiceTest {

    @Mock
    private ResenaRepository resenaRepository;

    @Mock
    private ResenaValidaciones resenaValidaciones;

    @InjectMocks
    private ResenaService resenaService;

    private Resena resena;

    @BeforeEach
    void setUp() {
        resena = new Resena();
        resena.setIdResena(1);
        resena.setPuntuacion(5);
        resena.setComentario("Excelente herramienta");
        resena.setFechaResena(LocalDate.of(2026, 6, 25));
        resena.setRutUsuario("12345678-9");
        resena.setHerramientaId(1);
        resena.setReservaId(1);
    }

    @Test
    void obtenerTodosDebeRetornarListaDeResenas() {
        // Given
        when(resenaRepository.findAll()).thenReturn(List.of(resena));

        // When
        List<ResenaDTO> resultado = resenaService.obtenerTodos();

        // Then
        assertEquals(1, resultado.size());
        assertEquals(1, resultado.get(0).getIdResena());
        assertEquals("Excelente herramienta", resultado.get(0).getComentario());
        assertEquals(5, resultado.get(0).getPuntuacion());

        verify(resenaRepository).findAll();
    }

    @Test
    void buscarPorIdDebeRetornarResenaExistente() {
        // Given
        when(resenaRepository.findById(1)).thenReturn(Optional.of(resena));

        // When
        ResenaDTO resultado = resenaService.buscarPorId(1);

        // Then
        assertNotNull(resultado);
        assertEquals(1, resultado.getIdResena());
        assertEquals("Excelente herramienta", resultado.getComentario());

        verify(resenaRepository).findById(1);
    }

    @Test
    void buscarPorIdDebeLanzarErrorCuandoNoExiste() {
        // Given
        when(resenaRepository.findById(99)).thenReturn(Optional.empty());

        // When / Then
        assertThrows(RuntimeException.class, () -> resenaService.buscarPorId(99));

        verify(resenaRepository).findById(99);
    }

    @Test
    void buscarPorRutUsuarioDebeRetornarResenasDelUsuario() {
        // Given
        when(resenaRepository.findByRutUsuario("12345678-9")).thenReturn(List.of(resena));

        // When
        List<ResenaDTO> resultado = resenaService.buscarPorRutUsuario("12345678-9");

        // Then
        assertEquals(1, resultado.size());
        assertEquals("12345678-9", resultado.get(0).getRutUsuario());

        verify(resenaRepository).findByRutUsuario("12345678-9");
    }

    @Test
    void buscarPorHerramientaIdDebeRetornarResenasDeHerramienta() {
        // Given
        when(resenaRepository.findByHerramientaId(1)).thenReturn(List.of(resena));

        // When
        List<ResenaDTO> resultado = resenaService.buscarPorHerramientaId(1);

        // Then
        assertEquals(1, resultado.size());
        assertEquals(1, resultado.get(0).getHerramientaId());

        verify(resenaRepository).findByHerramientaId(1);
    }

    @Test
    void buscarPorReservaIdDebeRetornarResenasDeReserva() {
        // Given
        when(resenaRepository.findByReservaId(1)).thenReturn(List.of(resena));

        // When
        List<ResenaDTO> resultado = resenaService.buscarPorReservaId(1);

        // Then
        assertEquals(1, resultado.size());
        assertEquals(1, resultado.get(0).getReservaId());

        verify(resenaRepository).findByReservaId(1);
    }

    @Test
    void guardarDebeCrearResenaCuandoDatosSonValidos() {
        // Given
        doNothing().when(resenaValidaciones).validarPuntuacion(resena);
        doNothing().when(resenaValidaciones).validarReservaExistente(1);
        doNothing().when(resenaValidaciones).validarHerramientaExistente(1);

        when(resenaRepository.save(resena)).thenReturn(resena);

        // When
        ResenaDTO resultado = resenaService.guardar(resena);

        // Then
        assertNotNull(resultado);
        assertEquals(1, resultado.getIdResena());
        assertEquals(5, resultado.getPuntuacion());
        assertEquals("Excelente herramienta", resultado.getComentario());

        verify(resenaValidaciones).validarPuntuacion(resena);
        verify(resenaValidaciones).validarReservaExistente(1);
        verify(resenaValidaciones).validarHerramientaExistente(1);
        verify(resenaRepository).save(resena);
    }

    @Test
    void guardarDebeAsignarFechaCuandoNoVieneInformada() {
        // Given
        resena.setFechaResena(null);

        doNothing().when(resenaValidaciones).validarPuntuacion(resena);
        doNothing().when(resenaValidaciones).validarReservaExistente(1);
        doNothing().when(resenaValidaciones).validarHerramientaExistente(1);

        when(resenaRepository.save(resena)).thenReturn(resena);

        // When
        ResenaDTO resultado = resenaService.guardar(resena);

        // Then
        assertNotNull(resultado);
        assertNotNull(resultado.getFechaResena());

        verify(resenaRepository).save(resena);
    }

    @Test
    void actualizarDebeModificarResenaCuandoExiste() {
        // Given
        Resena datosActualizados = new Resena();
        datosActualizados.setPuntuacion(4);
        datosActualizados.setComentario("Buena herramienta");
        datosActualizados.setRutUsuario("12345678-9");
        datosActualizados.setHerramientaId(1);
        datosActualizados.setReservaId(1);

        when(resenaRepository.findById(1)).thenReturn(Optional.of(resena));

        doNothing().when(resenaValidaciones).validarHerramientaExistente(1);
        doNothing().when(resenaValidaciones).validarReservaExistente(1);
        doNothing().when(resenaValidaciones).validarPuntuacion(resena);

        when(resenaRepository.save(resena)).thenReturn(resena);

        // When
        ResenaDTO resultado = resenaService.actualizar(1, datosActualizados);

        // Then
        assertNotNull(resultado);
        assertEquals(4, resultado.getPuntuacion());
        assertEquals("Buena herramienta", resultado.getComentario());

        verify(resenaRepository).findById(1);
        verify(resenaRepository).save(resena);
    }

    @Test
    void actualizarDebeLanzarErrorCuandoNoExiste() {
        // Given
        Resena datosActualizados = new Resena();
        datosActualizados.setComentario("Comentario actualizado");

        when(resenaRepository.findById(99)).thenReturn(Optional.empty());

        // When / Then
        assertThrows(RuntimeException.class, () -> resenaService.actualizar(99, datosActualizados));

        verify(resenaRepository).findById(99);
    }

    @Test
    void eliminarDebeRetornarMensajeCuandoExiste() {
        // Given
        when(resenaRepository.findById(1)).thenReturn(Optional.of(resena));

        // When
        String resultado = resenaService.eliminar(1);

        // Then
        assertEquals("La resena con ID 1 fue eliminada correctamente.", resultado);

        verify(resenaRepository).findById(1);
        verify(resenaRepository).delete(resena);
    }

    @Test
    void eliminarDebeLanzarErrorCuandoNoExiste() {
        // Given
        when(resenaRepository.findById(99)).thenReturn(Optional.empty());

        // When / Then
        assertThrows(RuntimeException.class, () -> resenaService.eliminar(99));

        verify(resenaRepository).findById(99);
    }
}


