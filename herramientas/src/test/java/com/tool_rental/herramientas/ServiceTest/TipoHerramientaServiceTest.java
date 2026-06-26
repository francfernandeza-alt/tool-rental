package com.tool_rental.herramientas.ServiceTest;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.tool_rental.herramientas.DTO.TipoHerramientaDTO;
import com.tool_rental.herramientas.Model.TipoHerramienta;
import com.tool_rental.herramientas.Repository.TipoHerramientaRepository;
import com.tool_rental.herramientas.Service.TipoHerramientaService;

import net.datafaker.Faker;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TipoHerramientaServiceTest {
    @Mock
    private TipoHerramientaRepository tipoHerramientaRepository;

    @InjectMocks
    private TipoHerramientaService tipoHerramientaService;

    private final Faker faker = new Faker();

    @Test
    void findAll_Exitoso() {
    //Given
        String nombre = faker.commerce().department();
        String descripcion = faker.lorem().sentence();
        TipoHerramienta tipoFake = new TipoHerramienta();
        tipoFake.setIdTipoHerramienta(1);
        tipoFake.setNombreTipoHerramienta(nombre);
        tipoFake.setDescripcionTipoHerramienta(descripcion);
        when(tipoHerramientaRepository.findAll()).thenReturn(List.of(tipoFake));
    //When
        List<TipoHerramientaDTO> resultado = tipoHerramientaService.findAll();
    //Then
        assertNotNull(resultado, "La lista no debe ser nula");
        assertEquals(1, resultado.size(), "Debe retornar exactamente un registro");
        assertEquals(nombre, resultado.get(0).getNombreTipoHerramientaDTO(), "El nombre no coincide");
        assertEquals(descripcion, resultado.get(0).getDescripcionTipoHerramientaDTO(), "La descripción no se mapeó correctamente");
        verify(tipoHerramientaRepository, times(1)).findAll();
    }

    @Test
    void findAll_Vacio() {
    //Given
        when(tipoHerramientaRepository.findAll()).thenReturn(Collections.emptyList());
    //When
        List<TipoHerramientaDTO> resultado = tipoHerramientaService.findAll();
    //Then
        assertNotNull(resultado, "Aunque no haya datos, la lista debe estar instanciada");
        assertEquals(0, resultado.size(), "La lista debe estar vacía");
        verify(tipoHerramientaRepository, times(1)).findAll();
    }

    @Test
    void findById_Exitoso() {
    //Given
        Integer idSimulado = 1;
        String nombre = faker.commerce().department();
        String descripcion = faker.lorem().sentence();
        TipoHerramienta tipo = new TipoHerramienta();
        tipo.setIdTipoHerramienta(idSimulado);
        tipo.setNombreTipoHerramienta(nombre);
        tipo.setDescripcionTipoHerramienta(descripcion);
        when(tipoHerramientaRepository.findById(idSimulado)).thenReturn(Optional.of(tipo));
    //When
        TipoHerramientaDTO resultado = tipoHerramientaService.findById(idSimulado);
    //Then
        assertNotNull(resultado, "El DTO resultante no debería ser nulo.");
        assertEquals(idSimulado, resultado.getIdTipoHerramientaDTO(), "El ID debe coincidir.");
        assertEquals(nombre, resultado.getNombreTipoHerramientaDTO(), "El nombre transformado debe coincidir con el de la base de datos");
        assertEquals(descripcion, resultado.getDescripcionTipoHerramientaDTO(), "La descripción debe mapearse correctamente.");
        verify(tipoHerramientaRepository, times(1)).findById(idSimulado);
    }

    @Test
    void findById_IdNulo() {
    //Given
        Integer idNulo = null;
    //When
        RuntimeException excepcion = assertThrows(RuntimeException.class, () -> {
            tipoHerramientaService.findById(idNulo);
        });
    //Then
        assertEquals("El ID de búsqueda no puede ser nulo", excepcion.getMessage());
        verify(tipoHerramientaRepository, times(0)).findById(any());
    }

    @Test
    void findById_IdNoExiste() {
    //Given
        Integer idNoExiste = 99;
        when(tipoHerramientaRepository.findById(idNoExiste)).thenReturn(Optional.empty());
    //When & Then
        RuntimeException excepcion = assertThrows(RuntimeException.class, () -> {
            tipoHerramientaService.findById(idNoExiste);
        });
        assertEquals("Tipo de herramienta no encontrado", excepcion.getMessage());
        verify(tipoHerramientaRepository, times(1)).findById(idNoExiste);
    }

    @Test
    void save_Exitoso() {
    //Given
        String nombre = faker.commerce().department();
        String descripcion = faker.lorem().sentence();
        
        TipoHerramientaDTO dto = new TipoHerramientaDTO();
        dto.setNombreTipoHerramientaDTO(nombre);
        dto.setDescripcionTipoHerramientaDTO(descripcion);
        
        TipoHerramienta guardado = new TipoHerramienta();
        guardado.setIdTipoHerramienta(10);
        guardado.setNombreTipoHerramienta(nombre);
        guardado.setDescripcionTipoHerramienta(descripcion);
        
        when(tipoHerramientaRepository.save(any(TipoHerramienta.class))).thenReturn(guardado);
    //When
        TipoHerramientaDTO resultado = tipoHerramientaService.save(dto);
    //Then
        assertNotNull(resultado, "El DTO resultante no deberia ser nulo.");
        assertEquals(10, resultado.getIdTipoHerramientaDTO(), "El ID generado debe coincidir.");
        assertEquals(nombre, resultado.getNombreTipoHerramientaDTO(), "El nombre debe ser el mismo que el ingresado.");
        verify(tipoHerramientaRepository, times(1)).save(any(TipoHerramienta.class));
    }

    @Test
    void save_NombreVacio() {
    //Given
        TipoHerramientaDTO dto = new TipoHerramientaDTO();
        dto.setNombreTipoHerramientaDTO("   ");
    //When & Then
        RuntimeException excepcion = assertThrows(RuntimeException.class, () -> {
            tipoHerramientaService.save(dto);
        });
        assertEquals("El nombre del tipo de herramienta es obligatorio", excepcion.getMessage());
        verify(tipoHerramientaRepository, times(0)).save(any());
    }

    @Test
    void save_DescripcionLarga() {
    //Given
        TipoHerramientaDTO dto = new TipoHerramientaDTO();
        dto.setNombreTipoHerramientaDTO(faker.commerce().department());
        dto.setDescripcionTipoHerramientaDTO(faker.lorem().characters(300));
    //When & Then
        RuntimeException excepcion = assertThrows(RuntimeException.class, () -> {
            tipoHerramientaService.save(dto);
        });
        assertEquals("La descripción es opcional, pero no puede superar los 255 caracteres.", excepcion.getMessage());
        verify(tipoHerramientaRepository, times(0)).save(any());
    }

    @Test
    void actualizarTipoHerramienta_Exitoso() {
    //Given
        Integer idSimulado = 5;
        String nombreOriginal = faker.commerce().department();
        String nombreNuevo = faker.commerce().department() + " Premium";
        String descripcionNueva = faker.lorem().sentence();
        
        TipoHerramientaDTO dto = new TipoHerramientaDTO();
        dto.setNombreTipoHerramientaDTO(nombreNuevo);
        dto.setDescripcionTipoHerramientaDTO(descripcionNueva);
        
        TipoHerramienta original = new TipoHerramienta();
        original.setIdTipoHerramienta(idSimulado);
        original.setNombreTipoHerramienta(nombreOriginal);
        
        TipoHerramienta actualizada = new TipoHerramienta();
        actualizada.setIdTipoHerramienta(idSimulado);
        actualizada.setNombreTipoHerramienta(nombreNuevo);
        actualizada.setDescripcionTipoHerramienta(descripcionNueva);
        
        when(tipoHerramientaRepository.findById(idSimulado)).thenReturn(Optional.of(original));
        when(tipoHerramientaRepository.save(any(TipoHerramienta.class))).thenReturn(actualizada);
    //When
        TipoHerramientaDTO resultado = tipoHerramientaService.actualizarTipoHerramienta(idSimulado, dto);
    //Then
        assertNotNull(resultado, "El resultado no debe ser nulo");
        assertEquals(nombreNuevo, resultado.getNombreTipoHerramientaDTO(), "El nombre debe haberse actualizado correctamente");
        assertEquals(descripcionNueva, resultado.getDescripcionTipoHerramientaDTO());
        verify(tipoHerramientaRepository, times(1)).findById(idSimulado);
        verify(tipoHerramientaRepository, times(1)).save(any(TipoHerramienta.class));
    }

    @Test
    void actualizarTipoHerramienta_IdNoExiste() {
    //Given
        Integer idSimulado = 99;
        when(tipoHerramientaRepository.findById(idSimulado)).thenReturn(Optional.empty());
    //When & Then
        assertThrows(RuntimeException.class, () -> {
            tipoHerramientaService.actualizarTipoHerramienta(idSimulado, new TipoHerramientaDTO());
        });
        verify(tipoHerramientaRepository, times(1)).findById(idSimulado);
        verify(tipoHerramientaRepository, times(0)).save(any(TipoHerramienta.class));
    }

    @Test
    void actualizarTipoHerramienta_FalloValidacion() {
    //Given
        Integer idSimulado = 1;
        TipoHerramienta original = new TipoHerramienta();
        original.setIdTipoHerramienta(idSimulado);
        TipoHerramientaDTO dto = new TipoHerramientaDTO();
        dto.setNombreTipoHerramientaDTO("   ");
        when(tipoHerramientaRepository.findById(idSimulado)).thenReturn(Optional.of(original));
    //When
        assertThrows(RuntimeException.class, () -> {
            tipoHerramientaService.actualizarTipoHerramienta(idSimulado, dto);
        });
        verify(tipoHerramientaRepository, times(0)).save(any());
    }

    @Test
    void eliminar_Exitoso() {
        //Given
        Integer idSimulado = 1;
        TipoHerramienta tipo = new TipoHerramienta();
        tipo.setIdTipoHerramienta(idSimulado);
        when(tipoHerramientaRepository.findById(idSimulado)).thenReturn(Optional.of(tipo));
    //When
        String mensaje = tipoHerramientaService.eliminar(idSimulado);
    //Then
        assertEquals("El tipo de herramienta con Id " + idSimulado + " ha sido eliminado exitosamente.", mensaje);
        verify(tipoHerramientaRepository, times(1)).findById(idSimulado);
        verify(tipoHerramientaRepository, times(1)).delete(tipo);
    }

    @Test
    void eliminar_IdNoExiste() {
    //Given
        Integer idSimulado = 99;
        when(tipoHerramientaRepository.findById(idSimulado)).thenReturn(Optional.empty());
    //When & Then
        RuntimeException excepcion = assertThrows(RuntimeException.class, () -> {
            tipoHerramientaService.eliminar(idSimulado);
        });
        assertEquals("El tipo de herramienta no existe", excepcion.getMessage());
        verify(tipoHerramientaRepository, times(1)).findById(idSimulado);
        verify(tipoHerramientaRepository, times(0)).delete(any(TipoHerramienta.class));
    }
}
