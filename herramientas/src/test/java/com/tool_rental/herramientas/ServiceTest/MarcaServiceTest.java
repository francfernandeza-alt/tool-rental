package com.tool_rental.herramientas.ServiceTest;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.tool_rental.herramientas.DTO.MarcaDTO;
import com.tool_rental.herramientas.Model.Marca;
import com.tool_rental.herramientas.Repository.MarcaRepository;
import com.tool_rental.herramientas.Service.MarcaService;

import net.datafaker.Faker;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MarcaServiceTest {
    @Mock
    private MarcaRepository marcaRepository;

    @InjectMocks
    private MarcaService marcaService;

    private final Faker faker = new Faker();

    @Test
    void obtenerTodos_Exitoso() {
    //Given
        Marca marcaFake = new Marca();
        marcaFake.setIdMarca(1);
        marcaFake.setNombreMarca(faker.company().name());
        when(marcaRepository.findAll()).thenReturn(List.of(marcaFake));
    //When
        List<MarcaDTO> resultado = marcaService.obtenerTodos();
    //Then
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(marcaFake.getNombreMarca(), resultado.get(0).getNombreMarcaDTO());
        verify(marcaRepository, times(1)).findAll();
    }

    @Test
    void obtenerTodos_Vacio() {
    //Given
        when(marcaRepository.findAll()).thenReturn(Collections.emptyList());
    //When
        List<MarcaDTO> resultado = marcaService.obtenerTodos();
    //Then
        assertNotNull(resultado);
        assertEquals(0, resultado.size());
        verify(marcaRepository, times(1)).findAll();
    }

    @Test
    void buscarporId_Exitoso() {
    //Given
        Integer idSimulado = 1;
        String nombreMarcaAleatoria = faker.company().name();
        Marca marca = new Marca();
        marca.setIdMarca(idSimulado);
        marca.setNombreMarca(nombreMarcaAleatoria);
        when(marcaRepository.findById(idSimulado)).thenReturn(Optional.of(marca));
    //When
        MarcaDTO resultado = marcaService.buscarporId(idSimulado);
    //Then
        assertNotNull(resultado, "El DTO resultante no debería ser nulo.");
        assertEquals(idSimulado, resultado.getIdMarcaDTO(), "El ID debe coincidir.");
        assertEquals(nombreMarcaAleatoria, resultado.getNombreMarcaDTO(), "El nombre transformado debe coincidir con el de la base de datos");
        verify(marcaRepository, times(1)).findById(idSimulado);
    }

    @Test
    void buscarporId_IdNulo() {
    //Given
        Integer idNulo = null;
    //When
        RuntimeException excepcion = assertThrows(RuntimeException.class, () -> {
            marcaService.buscarporId(idNulo);
        });
    //Then
        assertEquals("El ID de búsqueda no puede ser nulo", excepcion.getMessage());
    }

    @Test
    void buscarporId_VerificarDTO_Exitoso() {
    //Given
        Integer idSimulado = 1;
        String nombre = faker.company().name();
        Marca marca = new Marca();
        marca.setIdMarca(idSimulado);
        marca.setNombreMarca(nombre);
        when(marcaRepository.findById(idSimulado)).thenReturn(Optional.of(marca));
    //When
        MarcaDTO resultadoDTO = marcaService.buscarporId(idSimulado);
    //Then
        assertNotNull(resultadoDTO);
        assertEquals(marca.getIdMarca(), resultadoDTO.getIdMarcaDTO(), "El ID no se mapeó correctamente");
        assertEquals(marca.getNombreMarca(), resultadoDTO.getNombreMarcaDTO(), "El nombre no se mapeó correctamente");
    }

    @Test
    void buscarporId_IdNoExiste() {
    //Given
        Integer idNoExiste = 99;
        when(marcaRepository.findById(idNoExiste)).thenReturn(Optional.empty());
    //When & Then
        RuntimeException excepcion = assertThrows(RuntimeException.class, () -> {
            marcaService.buscarporId(idNoExiste);
        });
        assertEquals("Marca no encontrada", excepcion.getMessage());
        verify(marcaRepository, times(1)).findById(idNoExiste);
    }

    @Test
    void guardarMarca_Exitoso() {
    //Given
        String nombreMarcaAleatoria = faker.company().name();
        MarcaDTO marcaDTO = new MarcaDTO();
        marcaDTO.setNombreMarcaDTO(nombreMarcaAleatoria);
        Marca marcaGuardada = new Marca();
        marcaGuardada.setIdMarca(10);
        marcaGuardada.setNombreMarca(nombreMarcaAleatoria);
        when(marcaRepository.save(any(Marca.class))).thenReturn(marcaGuardada);
    //When
        MarcaDTO resultado = marcaService.guardarMarca(marcaDTO);
    //Then
        assertNotNull(resultado, "El DTO resultante no deberia ser nulo.");
        assertEquals(10, resultado.getIdMarcaDTO(), "El ID generado debe coincidir.");
        assertEquals(nombreMarcaAleatoria, resultado.getNombreMarcaDTO(), "El nombre debe ser el mismo que el ingresado.");
        verify(marcaRepository, times(1)).save(any(Marca.class));
    }

    @Test
    void guardarMarca_NombreVacio() {
    //Given
        MarcaDTO marcaDTO = new MarcaDTO();
        marcaDTO.setNombreMarcaDTO("   ");
    //When & Then
        RuntimeException excepcion = assertThrows(RuntimeException.class, () -> {
            marcaService.guardarMarca(marcaDTO);
        });
        assertEquals("El nombre de la marca no puede estar vacío.", excepcion.getMessage());
    }

    @Test
    void guardarMarca_NombreCorto() {
    //Given
        MarcaDTO marcaDTO = new MarcaDTO();
        marcaDTO.setNombreMarcaDTO("HP");
    //When & Then
        RuntimeException excepcion = assertThrows(RuntimeException.class, () -> {
            marcaService.guardarMarca(marcaDTO);
        });
        assertEquals("El nombre de la marca debe tener al menos 3 caracteres.", excepcion.getMessage());
    }

    @Test
    void actualizarMarca_Exitoso() {
    //Given
        Integer idSimulado = 5;
        String nombreOriginal = faker.company().name();
        String nombreNuevo = faker.company().name() + " Pro";
        MarcaDTO marcadto = new MarcaDTO();
        marcadto.setNombreMarcaDTO(nombreNuevo);
        Marca marca = new Marca();
        marca.setIdMarca(idSimulado);
        marca.setNombreMarca(nombreOriginal);
        Marca marcaActualizada = new Marca();
        marcaActualizada.setIdMarca(idSimulado);
        marcaActualizada.setNombreMarca(nombreNuevo);
        when(marcaRepository.findById(idSimulado)).thenReturn(Optional.of(marca));
        when(marcaRepository.save(any(Marca.class))).thenReturn(marcaActualizada);
    //When
        MarcaDTO resultado = marcaService.actualizarMarca(idSimulado, marcadto);
    //Then
        assertNotNull(resultado, "El resultado no debe ser nulo");
        assertEquals(nombreNuevo, resultado.getNombreMarcaDTO(), "El nombre debe haberse actualizado correctamente");
        verify(marcaRepository, times(1)).findById(idSimulado);
        verify(marcaRepository, times(1)).save(any(Marca.class));
    }

    @Test
    void actualizarMarca_IdNoExiste() {
    //Given
        Integer idSimulado = 99;
        MarcaDTO marcaDTO = new MarcaDTO();
        marcaDTO.setNombreMarcaDTO(faker.company().name());
        when(marcaRepository.findById(idSimulado)).thenReturn(Optional.empty());
    //Then & When
        RuntimeException excepcion = assertThrows(RuntimeException.class, () -> {
            marcaService.actualizarMarca(idSimulado, marcaDTO);
        });
        assertEquals("Marca no existe", excepcion.getMessage());
        verify(marcaRepository, times(1)).findById(idSimulado);
        verify(marcaRepository, times(0)).save(any(Marca.class));
    }

    @Test
    void eliminar_Exitoso() {
    // Given
        Integer idSimulado = 1;
        String nombreMarcaAleatoria = faker.company().name();
        Marca marca = new Marca();
        marca.setIdMarca(idSimulado);
        marca.setNombreMarca(nombreMarcaAleatoria);
        when(marcaRepository.findById(idSimulado)).thenReturn(Optional.of(marca));
    //When
        String mensaje = marcaService.eliminar(idSimulado);
    //Then
        assertEquals("Marca " + nombreMarcaAleatoria + " ha sido eliminada.", mensaje);
        verify(marcaRepository, times(1)).findById(idSimulado);
        verify(marcaRepository, times(1)).delete(marca);
    }

    @Test
    void eliminar_IdNoExiste() {
    //Given
        Integer idSimulado = 45;
        when(marcaRepository.findById(idSimulado)).thenReturn(Optional.empty());
    //When & Then
        RuntimeException excepcion = assertThrows(RuntimeException.class, () -> {
            marcaService.eliminar(idSimulado);
        });
        assertEquals("Marca con Id 45 no existe.", excepcion.getMessage());
        verify(marcaRepository, times(1)).findById(idSimulado);
        verify(marcaRepository, times(0)).delete(any(Marca.class));
    }
}
