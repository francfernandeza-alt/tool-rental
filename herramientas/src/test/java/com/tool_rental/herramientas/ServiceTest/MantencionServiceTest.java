package com.tool_rental.herramientas.ServiceTest;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.tool_rental.herramientas.DTO.MantencionDTO;
import com.tool_rental.herramientas.Model.Mantencion;
import com.tool_rental.herramientas.Repository.MantencionRepository;
import com.tool_rental.herramientas.Service.MantencionService;

import net.datafaker.Faker;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MantencionServiceTest {
    @Mock
    private MantencionRepository mantencionRepository;

    @InjectMocks
    private MantencionService mantencionService;

    private final Faker faker = new Faker();

    @Test
    void findAll_Exitoso() {
    //Given
        Mantencion mantencionFake = new Mantencion();
        mantencionFake.setIdMantencion(1);
        mantencionFake.setFechaUltimaMantencion(LocalDate.now().minusDays(5));
        mantencionFake.setVigenciaMeses(6);
        mantencionFake.setDescripcion(faker.lorem().sentence());
        mantencionFake.setEstado("VIGENTE");
        when(mantencionRepository.findAll()).thenReturn(List.of(mantencionFake));
    //When
        List<MantencionDTO> resultado = mantencionService.findAll();
    //Then
        assertNotNull(resultado, "La lista retornada no debe ser nula");
        assertEquals(1, resultado.size(), "Debe haber exactamente un registro");
        assertEquals(mantencionFake.getIdMantencion(), resultado.get(0).getIdMantencion(), "El ID debe coincidir");
        assertEquals(mantencionFake.getDescripcion(), resultado.get(0).getDescripcion(), "La descripción debe mapearse correctamente");
        assertEquals("VIGENTE", resultado.get(0).getEstado(), "El estado debe ser VIGENTE");
        verify(mantencionRepository, times(1)).findAll();
    }

    @Test
    void findAll_Vacio() {
    //Given
        when(mantencionRepository.findAll()).thenReturn(Collections.emptyList());
    //When
        List<MantencionDTO> resultado = mantencionService.findAll();
    //Then
        assertNotNull(resultado);
        assertEquals(0, resultado.size(), "La lista debe estar vacía pero no ser nula");
        verify(mantencionRepository, times(1)).findAll();
    }

    @Test
    void findById_Exitoso() {
    //Given
        Integer idSimulado = faker.number().positive();
        String descripcionAleatoria = faker.lorem().sentence();
        Mantencion mantencion = new Mantencion();
        mantencion.setIdMantencion(idSimulado);
        mantencion.setFechaUltimaMantencion(LocalDate.now().minusDays(2));
        mantencion.setVigenciaMeses(12);
        mantencion.setDescripcion(descripcionAleatoria);
        mantencion.setEstado("VIGENTE");
        when(mantencionRepository.findById(idSimulado)).thenReturn(Optional.of(mantencion));
    //When
        MantencionDTO resultado = mantencionService.findById(idSimulado);
    //Then
        assertNotNull(resultado, "El DTO resultante no debería ser nulo.");
        assertEquals(idSimulado, resultado.getIdMantencion(), "El ID debe coincidir.");
        assertEquals(descripcionAleatoria, resultado.getDescripcion(),  "La descripción debe mapearse correctamente.");
        assertEquals("VIGENTE", resultado.getEstado());
        verify(mantencionRepository, times(1)).findById(idSimulado);
    }

    @Test
    void findById_IdNulo() {
    //Given
        Integer idNulo = null;
    //When & Then
        RuntimeException excepcion = assertThrows(RuntimeException.class, () -> {
            mantencionService.findById(idNulo);
        });
        //Then
        assertEquals("El ID de búsqueda no puede ser nulo", excepcion.getMessage());
        verify(mantencionRepository, times(0)).findById(any());
    }

    @Test
    void findById_IdNoExiste() {
    //Given
        Integer idNoExiste = 99;
        when(mantencionRepository.findById(idNoExiste)).thenReturn(Optional.empty());
    //When & Then
        RuntimeException excepcion = assertThrows(RuntimeException.class, () -> {
            mantencionService.findById(idNoExiste);
        });
        assertEquals("Mantención con ID " + idNoExiste + " no encontrada en el sistema", excepcion.getMessage());
        verify(mantencionRepository, times(1)).findById(idNoExiste);
    }

    @Test
    void save_Exitoso() {
    //Given
        String descripcionAleatoria = faker.lorem().sentence();
        MantencionDTO mantencionDTO = new MantencionDTO();
        mantencionDTO.setFechaUltimaMantencion(LocalDate.now().minusDays(1));
        mantencionDTO.setVigenciaMeses(3);
        mantencionDTO.setDescripcion(descripcionAleatoria);
        mantencionDTO.setEstado("PROGRAMADA");

        Mantencion mantencionGuardada = new Mantencion();
        mantencionGuardada.setIdMantencion(10);
        mantencionGuardada.setFechaUltimaMantencion(LocalDate.now().minusDays(1));
        mantencionGuardada.setVigenciaMeses(3);
        mantencionGuardada.setDescripcion(descripcionAleatoria);
        mantencionGuardada.setEstado("PROGRAMADA");

        when(mantencionRepository.save(any(Mantencion.class))).thenReturn(mantencionGuardada);
    //When
        MantencionDTO resultado = mantencionService.save(mantencionDTO);
    //Then
        assertNotNull(resultado, "El DTO resultante no deberia ser nulo.");
        assertEquals(10, resultado.getIdMantencion(), "El ID generado debe coincidir.");
        assertEquals(descripcionAleatoria, resultado.getDescripcion());
        verify(mantencionRepository, times(1)).save(any(Mantencion.class));
    }

    @Test
    void save_FechaFutura() {
    //Given
        MantencionDTO mantencionDTO = new MantencionDTO();
        mantencionDTO.setFechaUltimaMantencion(LocalDate.now().plusDays(5));
        mantencionDTO.setVigenciaMeses(6);
        mantencionDTO.setDescripcion("Mantención de Motor");
        mantencionDTO.setEstado("VIGENTE");
    //When & Then
        RuntimeException excepcion = assertThrows(RuntimeException.class, () -> {
            mantencionService.save(mantencionDTO);
        });
        assertEquals("La fecha de mantención no puede ser futura", excepcion.getMessage());
        verify(mantencionRepository, times(0)).save(any());
    }

    @Test
    void save_VigenciaInvalida() {
    //Given
        MantencionDTO mantencionDTO = new MantencionDTO();
        mantencionDTO.setFechaUltimaMantencion(LocalDate.now());
        mantencionDTO.setVigenciaMeses(0);
        mantencionDTO.setDescripcion("Revisión de cables");
    //When & Then
        RuntimeException excepcion = assertThrows(RuntimeException.class, () -> {
            mantencionService.save(mantencionDTO);
        });
        assertEquals("La vigencia debe ser mayor a 0 meses", excepcion.getMessage());
        verify(mantencionRepository, times(0)).save(any());
    }

    @Test
    void save_EstadoInvalido() {
    //Given
        MantencionDTO mantencionDTO = new MantencionDTO();
        mantencionDTO.setFechaUltimaMantencion(LocalDate.now());
        mantencionDTO.setVigenciaMeses(12);
        mantencionDTO.setDescripcion("Ajuste de pernos");
        mantencionDTO.setEstado("TERMINADO");
    //When & Then
        RuntimeException excepcion = assertThrows(RuntimeException.class, () -> {
            mantencionService.save(mantencionDTO);
        });
        assertEquals("Estado de mantención inválido", excepcion.getMessage());
        verify(mantencionRepository, times(0)).save(any());
    }

    @Test
    void actualizarMantencion_Exitoso() {
    //Given
        Integer idSimulado = 5;
        String descripcionOriginal = faker.lorem().sentence();
        String descripcionNueva = faker.lorem().sentence();
        
        MantencionDTO mantenciondto = new MantencionDTO();
        mantenciondto.setDescripcion(descripcionNueva);
        mantenciondto.setEstado("EXPIRADA");

        Mantencion mantencion = new Mantencion();
        mantencion.setIdMantencion(idSimulado);
        mantencion.setDescripcion(descripcionOriginal);
        mantencion.setEstado("VIGENTE");

        Mantencion mantencionActualizada = new Mantencion();
        mantencionActualizada.setIdMantencion(idSimulado);
        mantencionActualizada.setDescripcion(descripcionNueva);
        mantencionActualizada.setEstado("EXPIRADA");

        when(mantencionRepository.findById(idSimulado)).thenReturn(Optional.of(mantencion));
        when(mantencionRepository.save(any(Mantencion.class))).thenReturn(mantencionActualizada);
    //When
        MantencionDTO resultado = mantencionService.actualizarMantencion(idSimulado, mantenciondto);
    //Then
        assertNotNull(resultado, "El resultado no debe ser nulo");
        assertEquals(descripcionNueva, resultado.getDescripcion(), "La descripción debe haberse actualizado correctamente");
        assertEquals("EXPIRADA", resultado.getEstado());
        verify(mantencionRepository, times(1)).findById(idSimulado);
        verify(mantencionRepository, times(1)).save(any(Mantencion.class));
    }

    @Test
    void actualizarMantencion_IdNoExiste() {
    //Given
        Integer idSimulado = 99;
        when(mantencionRepository.findById(idSimulado)).thenReturn(Optional.empty());
    //When & Then
        assertThrows(RuntimeException.class, () -> {
            mantencionService.actualizarMantencion(idSimulado, new MantencionDTO());
        });
        verify(mantencionRepository, times(0)).save(any());
    }

    @Test
    void actualizarMantencion_FalloValidacion() {
    //Given
        Integer idSimulado = 1;
        Mantencion mantencion = new Mantencion();
        mantencion.setIdMantencion(idSimulado);
        MantencionDTO dto = new MantencionDTO();
        dto.setFechaUltimaMantencion(LocalDate.now().plusYears(1));
        when(mantencionRepository.findById(idSimulado)).thenReturn(Optional.of(mantencion));
    //When & Then
        assertThrows(RuntimeException.class, () -> {
            mantencionService.actualizarMantencion(idSimulado, dto);
        });
        verify(mantencionRepository, times(1)).findById(idSimulado);
        verify(mantencionRepository, times(0)).save(any());
    }

    @Test
    void eliminar_Exitoso() {
    //Given
        Integer idSimulado = 1;
        Mantencion mantencion = new Mantencion();
        mantencion.setIdMantencion(idSimulado);
        when(mantencionRepository.findById(idSimulado)).thenReturn(Optional.of(mantencion));
    //When
        String mensaje = mantencionService.eliminar(idSimulado);
    //Then
        assertEquals("La mantención con Id '" + idSimulado + "' ha sido eliminada exitosamente.", mensaje);
        verify(mantencionRepository, times(1)).findById(idSimulado);
        verify(mantencionRepository, times(1)).delete(mantencion);
    }

    @Test
    void eliminar_IdNoExiste() {
    //Given
        Integer idSimulado = 99;
        when(mantencionRepository.findById(idSimulado)).thenReturn(Optional.empty());
    //When & Then
        RuntimeException excepcion = assertThrows(RuntimeException.class, () -> {
            mantencionService.eliminar(idSimulado);
        });
        assertEquals("La mantención que intenta eliminar no existe en el sistema.", excepcion.getMessage());
        verify(mantencionRepository, times(1)).findById(idSimulado);
        verify(mantencionRepository, times(0)).delete(any(Mantencion.class));
    }
}

