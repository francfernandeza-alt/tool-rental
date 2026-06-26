package com.tool_rental.herramientas.ServiceTest;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.tool_rental.herramientas.DTO.MaterialDTO;
import com.tool_rental.herramientas.Model.Material;
import com.tool_rental.herramientas.Repository.MaterialRepository;
import com.tool_rental.herramientas.Service.MaterialService;

import net.datafaker.Faker;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MaterialServiceTest {
    @Mock
    private MaterialRepository materialRepository;

    @InjectMocks
    private MaterialService materialService;

    private final Faker faker = new Faker();

    @Test
    void obtenerTodos_Exitoso() {
    //Given
        String nombre = faker.commerce().material();
        String descripcion = faker.lorem().sentence();
        Material materialFake = new Material();
        materialFake.setIdMaterial(1);
        materialFake.setNombreMaterial(nombre);
        materialFake.setDescripcionMaterial(descripcion);
        when(materialRepository.findAll()).thenReturn(List.of(materialFake));
    //When
        List<MaterialDTO> resultado = materialService.obtenerTodos();
    //Then
        assertNotNull(resultado, "La lista no debe ser nula");
        assertEquals(1, resultado.size(), "Debe retornar exactamente un registro");
        assertEquals(nombre, resultado.get(0).getNombreMaterialDTO(), "El nombre no coincide");
        assertEquals(descripcion, resultado.get(0).getDescripcionMaterialDTO(), "La descripción no se mapeó correctamente");
        verify(materialRepository, times(1)).findAll();
    }

    @Test
    void obtenerTodos_Vacio() {
    //Given
        when(materialRepository.findAll()).thenReturn(Collections.emptyList());
    //When
        List<MaterialDTO> resultado = materialService.obtenerTodos();
    //Then
        assertNotNull(resultado, "La lista debe existir, no puede ser nula.");
        assertEquals(0, resultado.size(), "La lista debe estar vacía");
        verify(materialRepository, times(1)).findAll();
    }

    @Test
    void buscarPorId_Exitoso() {
    //Given
        Integer idSimulado = 1;
        String nombre = faker.commerce().material();
        String descripcion = faker.lorem().sentence();
        Material material = new Material();
        material.setIdMaterial(idSimulado);
        material.setNombreMaterial(nombre);
        material.setDescripcionMaterial(descripcion);
        when(materialRepository.findById(idSimulado)).thenReturn(Optional.of(material));
    //When
        MaterialDTO resultado = materialService.buscarPorId(idSimulado);
    //Then
        assertNotNull(resultado, "El DTO resultante no debería ser nulo.");
        assertEquals(idSimulado, resultado.getIdMaterialDTO(), "El ID debe coincidir.");
        assertEquals(nombre , resultado.getNombreMaterialDTO(), "El nombre debe coincidir con el de la base de datos");
        assertEquals(descripcion, resultado.getDescripcionMaterialDTO(), "La descripción debe mapearse correctamente.");
        verify(materialRepository, times(1)).findById(idSimulado);
    }

    @Test
    void buscarPorId_IdNulo() {
    //Given
        Integer idNulo = null;
    //When
        RuntimeException excepcion = assertThrows(RuntimeException.class, () -> {
            materialService.buscarPorId(idNulo);
        });
    //Then
        assertEquals("Debes proporcionar un ID válido para realizar la búsqueda.", excepcion.getMessage());
        verify(materialRepository, times(0)).findById(any());
    }

    @Test
    void buscarPorId_IdNoExiste() {
    //Given
        Integer idNoExiste = 99;
        when(materialRepository.findById(idNoExiste)).thenReturn(Optional.empty());
    //When & Then
        RuntimeException excepcion = assertThrows(RuntimeException.class, () -> {
            materialService.buscarPorId(idNoExiste);
        });
        assertEquals("Material no encontrado", excepcion.getMessage());
        verify(materialRepository, times(1)).findById(idNoExiste);
    }

    @Test
    void guardarMaterial_Exitoso() {
    //Given
        String nombre = faker.commerce().material();
        String descripcion = faker.lorem().sentence();
        MaterialDTO materialDTO = new MaterialDTO();
        materialDTO.setNombreMaterialDTO(nombre);
        materialDTO.setDescripcionMaterialDTO(descripcion);
        Material guardado = new Material();
        guardado.setIdMaterial(10);
        guardado.setNombreMaterial(nombre);
        guardado.setDescripcionMaterial(descripcion);
        when(materialRepository.save(any(Material.class))).thenReturn(guardado);
    //When
        MaterialDTO resultado = materialService.guardarMaterial(materialDTO);
    //Then
        assertNotNull(resultado, "El DTO resultante no deberia ser nulo.");
        assertEquals(10, resultado.getIdMaterialDTO(), "El ID generado debe coincidir.");
        assertEquals(nombre, resultado.getNombreMaterialDTO(), "El nombre debe ser el mismo que el ingresado.");
        assertEquals(descripcion, resultado.getDescripcionMaterialDTO(), "La descripción debe persistirse si se informa");
        verify(materialRepository, times(1)).save(any(Material.class));
    }

    @Test
    void guardarMaterial_NombreVacio() {
    //Given
        MaterialDTO materialDTO = new MaterialDTO();
        materialDTO.setNombreMaterialDTO("   ");
        materialDTO.setDescripcionMaterialDTO(faker.lorem().sentence());
    //When & Then
        RuntimeException excepcion = assertThrows(RuntimeException.class, () -> {
            materialService.guardarMaterial(materialDTO);
        });
        assertEquals("El nombre del material es obligatorio", excepcion.getMessage());
        verify(materialRepository, times(0)).save(any());
    }

    @Test
    void guardarMaterial_DescripcionVacia() {
    //Given
        MaterialDTO materialDTO = new MaterialDTO();
        materialDTO.setNombreMaterialDTO(faker.commerce().material());
        materialDTO.setDescripcionMaterialDTO(null);
        Material guardado = new Material();
        guardado.setIdMaterial(11);
        guardado.setDescripcionMaterial(faker.commerce().material() + "Dorado");
        when(materialRepository.save(any(Material.class))).thenReturn(guardado);
    //When
        MaterialDTO resultado = materialService.guardarMaterial(materialDTO);
    //Then
        assertNotNull(resultado);
        verify(materialRepository, times(1)).save(any(Material.class));
    }

    @Test
    void actualizarMaterial_Exitoso() {
    //Given
        Integer idSimulado = 5;
        String nombreOriginal = faker.commerce().material();
        String nombreNuevo = faker.commerce().material() + " Reforzado";
        String descripcion = faker.lorem().sentence();
        
        MaterialDTO materialdto = new MaterialDTO();
        materialdto.setNombreMaterialDTO(nombreNuevo);
        materialdto.setDescripcionMaterialDTO(descripcion);
        
        Material materialOriginal = new Material();
        materialOriginal.setIdMaterial(idSimulado);
        materialOriginal.setNombreMaterial(nombreOriginal);
        
        Material materialActualizado = new Material();
        materialActualizado.setIdMaterial(idSimulado);
        materialActualizado.setNombreMaterial(nombreNuevo);
        materialActualizado.setDescripcionMaterial(descripcion);
        when(materialRepository.findById(idSimulado)).thenReturn(Optional.of(materialOriginal));
        when(materialRepository.save(any(Material.class))).thenReturn(materialActualizado);
    //When
        MaterialDTO resultado = materialService.actualizarMaterial(idSimulado, materialdto);
    //Then
        assertNotNull(resultado, "El resultado no debe ser nulo");
        assertEquals(nombreNuevo, resultado.getNombreMaterialDTO(), "El nombre debe haberse actualizado correctamente");
        assertEquals(descripcion, resultado.getDescripcionMaterialDTO(), "La descripción debe haberse actualizado correctamente");
        verify(materialRepository, times(1)).findById(idSimulado);
        verify(materialRepository, times(1)).save(any(Material.class));
    }

    @Test
    void actualizarMaterial_IdNoExiste() {
    //Given
        Integer idSimulado = 99;
        when(materialRepository.findById(idSimulado)).thenReturn(Optional.empty());
    //When & Then
        assertThrows(RuntimeException.class, () -> {
            materialService.actualizarMaterial(idSimulado, new MaterialDTO());
        });
        verify(materialRepository, times(1)).findById(idSimulado);
        verify(materialRepository, times(0)).save(any(Material.class));
    }

    @Test
    void actualizarMaterial_FalloValidacion() {
    //Given
        Integer idSimulado = 1;
        Material material = new Material();
        material.setIdMaterial(idSimulado);
        MaterialDTO dto = new MaterialDTO();
        dto.setNombreMaterialDTO("   ");
        when(materialRepository.findById(idSimulado)).thenReturn(Optional.of(material));
    //When & Then
        assertThrows(RuntimeException.class, () -> {
            materialService.actualizarMaterial(idSimulado, dto);
        });
        verify(materialRepository, times(0)).save(any());
    }

    @Test
    void eliminar_Exitoso() {
    // Given
        Integer idSimulado = 1;
        String nombre = faker.commerce().material();
        Material material = new Material();
        material.setIdMaterial(idSimulado);
        material.setNombreMaterial(nombre);
        when(materialRepository.findById(idSimulado)).thenReturn(Optional.of(material));
    //When
        String mensaje = materialService.eliminar(idSimulado);
    //Then
        assertEquals("Material eliminado correctamente", mensaje);
        verify(materialRepository, times(1)).findById(idSimulado);
        verify(materialRepository, times(1)).delete(material);
    }

    @Test
    void eliminar_IdNoExiste() {
    //Given
        Integer idSimulado = 99;
        when(materialRepository.findById(idSimulado)).thenReturn(Optional.empty());
    //When & Then
        RuntimeException excepcion = assertThrows(RuntimeException.class, () -> {
            materialService.eliminar(idSimulado);
        });
        assertEquals("Material con Id 99 no existe.", excepcion.getMessage());
        verify(materialRepository, times(1)).findById(idSimulado);
        verify(materialRepository, times(0)).delete(any(Material.class));
    }
}
