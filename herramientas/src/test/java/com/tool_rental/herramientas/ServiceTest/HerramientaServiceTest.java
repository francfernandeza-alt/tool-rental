package com.tool_rental.herramientas.ServiceTest;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.web.reactive.function.client.WebClient;

import com.tool_rental.herramientas.DTO.HerramientaDTO;
import com.tool_rental.herramientas.DTO.ResenaDTO;
import com.tool_rental.herramientas.Model.Herramienta;
import com.tool_rental.herramientas.Repository.HerramientaRepository;
import com.tool_rental.herramientas.Service.HerramientaService;

import net.datafaker.Faker;
import reactor.core.publisher.Flux;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class HerramientaServiceTest {
    @Mock
    private HerramientaRepository herramientaRepository;

    @InjectMocks
    private HerramientaService herramientaService;

    @Mock
    private WebClient.Builder webClientBuilder;

    @Mock
    private WebClient webClient;

    @Mock
    private WebClient.RequestHeadersUriSpec requestHeadersUriSpec;

    @Mock
    private WebClient.RequestHeadersSpec requestHeadersSpec;

    @Mock
    private WebClient.ResponseSpec responseSpec;

    private final Faker faker = new Faker();

    @Test
    void obtenerTodos_Exitoso() {
    //Given
        String nombreHerramienta = faker.commerce().productName();
        Herramienta her = new Herramienta();
        her.setIdHerramienta(1);
        her.setNombreHerramienta(nombreHerramienta);
        ResenaDTO resena = new ResenaDTO();
        resena.setPuntuacion(5);
        when(herramientaRepository.findAll()).thenReturn(List.of(her));
        when(webClientBuilder.build()).thenReturn(webClient);
        when(webClient.get()).thenReturn((WebClient.RequestHeadersUriSpec) requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString())).thenReturn((WebClient.RequestHeadersSpec) requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToFlux(ResenaDTO.class)).thenReturn(Flux.just(resena));

        List<HerramientaDTO> resultado = herramientaService.obtenerTodos();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(5.0, resultado.get(0).getPromedioEvaluacion());
        assertEquals(1, resultado.get(0).getTotalResenas());
        
        verify(herramientaRepository, times(1)).findAll();
    }

    @Test
    void obtenerTodos_Vacio() {
    //Given
        when(herramientaRepository.findAll()).thenReturn(Collections.emptyList());
        //When
        List<HerramientaDTO> resultado = herramientaService.obtenerTodos();
    //Then
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
        verify(herramientaRepository, times(1)).findAll();
        verify(webClientBuilder, times(0)).build();
    }

    @Test
    void obtenerTodos_FalloWebClient_() {
    //Given
        Herramienta her = new Herramienta();
        her.setIdHerramienta(10);
        when(herramientaRepository.findAll()).thenReturn(List.of(her));
        when(webClientBuilder.build()).thenThrow(new RuntimeException("Servicio caído"));
    //When
        List<HerramientaDTO> resultado = herramientaService.obtenerTodos();
    //Then
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(0.0, resultado.get(0).getPromedioEvaluacion(), "Debe ser 0 ante fallo externo");
    }

    @Test
    void buscarPorId_Exitoso() {
    //Given
        Integer idSimulado = 1;
        String nombreAleatorio = faker.commerce().productName();
        Herramienta her = new Herramienta();
        her.setIdHerramienta(idSimulado);
        her.setNombreHerramienta(nombreAleatorio);
        her.setEstadoHerramienta("DISPONIBLE");
        her.setCantidadDisponible(5);
        ResenaDTO resena1 = new ResenaDTO();
        resena1.setPuntuacion(5);
        ResenaDTO resena2 = new ResenaDTO();
        resena2.setPuntuacion(4);
        List<ResenaDTO> listaResenas = List.of(resena1, resena2);
        when(herramientaRepository.findById(idSimulado)).thenReturn(Optional.of(her));
        when(webClientBuilder.build()).thenReturn(webClient);
        when(webClient.get()).thenReturn((WebClient.RequestHeadersUriSpec) requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString())).thenReturn((WebClient.RequestHeadersUriSpec)requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToFlux(ResenaDTO.class)).thenReturn(Flux.fromIterable(listaResenas));
    //When
        HerramientaDTO resultado = herramientaService.buscarPorId(idSimulado);
    //Then
        assertNotNull(resultado, "El DTO resultante no debería ser nulo.");
        assertEquals(idSimulado, resultado.getIdHerramientaDTO(), "El ID debe coincidir.");
        assertEquals(nombreAleatorio, resultado.getNombreHerramientaDTO(), "El nombre transformado debe coincidir.");
        assertEquals(2, resultado.getTotalResenas(), "Debe haber reconciliado 2 reseñas");
        assertEquals(4.5, resultado.getPromedioEvaluacion(), "El promedio (5+4)/2 debe ser 4.5");
        verify(herramientaRepository, times(1)).findById(idSimulado);
    }

    @Test
    void buscarPorId_IdNoExiste() {
    //Given
        Integer idNoExiste = 99;
        when(herramientaRepository.findById(idNoExiste)).thenReturn(Optional.empty());
    //When & Then
        RuntimeException excepcion = assertThrows(RuntimeException.class, () -> {
            herramientaService.buscarPorId(idNoExiste);
        });
        assertEquals("No se encontró la herramienta con el ID ingresado", excepcion.getMessage());
        verify(herramientaRepository, times(1)).findById(idNoExiste);
        verify(webClientBuilder, times(0)).build();
    }

    @Test
    void buscarPorId_FalloWebClient() {
    //Given
        Integer idSimulado = 1;
        Herramienta her = new Herramienta();
        her.setIdHerramienta(idSimulado);
        her.setNombreHerramienta("Taladro");
        when(herramientaRepository.findById(idSimulado)).thenReturn(Optional.of(her));
        when(webClientBuilder.build()).thenThrow(new RuntimeException("Error de conexión con el servicio externo"));
    //When
        HerramientaDTO resultado = herramientaService.buscarPorId(idSimulado);
    //Then
        assertNotNull(resultado);
        assertEquals(0, resultado.getTotalResenas(), "A pesar del fallo externo, el total de reseñas debe ser 0");
        assertEquals(0.0, resultado.getPromedioEvaluacion(), "El promedio debe ser 0.0 ante fallos");
        verify(herramientaRepository, times(1)).findById(idSimulado);
    }

    @Test
    void buscarPorId_ResenasVacias() {
    //Given
        Integer idSimulado = 1;
        Herramienta her = new Herramienta();
        her.setIdHerramienta(idSimulado);
        her.setNombreHerramienta("Sierra");
        when(herramientaRepository.findById(idSimulado)).thenReturn(Optional.of(her));
        when(webClientBuilder.build()).thenReturn(webClient);
        when(webClient.get()).thenReturn((WebClient.RequestHeadersUriSpec) requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString())).thenReturn((WebClient.RequestHeadersSpec) requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToFlux(ResenaDTO.class)).thenReturn(Flux.empty()); // Lista vacía
    //When
        HerramientaDTO resultado = herramientaService.buscarPorId(idSimulado);
    //Then
        assertNotNull(resultado);
        assertEquals(0, resultado.getTotalResenas());
        assertEquals(0.0, resultado.getPromedioEvaluacion());
    }

    @Test
    void guardarHerramienta_Exitoso() {
    //Given
        String nombreAleatorio = faker.commerce().productName();
        HerramientaDTO dto = new HerramientaDTO();
        dto.setNombreHerramientaDTO(nombreAleatorio);
        dto.setEstadoHerramientaDTO("DISPONIBLE");
        dto.setCantidadDisponibleDTO(10);

        Herramienta guardada = new Herramienta();
        guardada.setIdHerramienta(15);
        guardada.setNombreHerramienta(nombreAleatorio);
        guardada.setEstadoHerramienta("DISPONIBLE");
        guardada.setCantidadDisponible(10);
        when(herramientaRepository.save(any(Herramienta.class))).thenReturn(guardada);
    //When
        HerramientaDTO resultado = herramientaService.guardarHerramienta(dto);
    //Then
        assertNotNull(resultado, "El DTO resultante no deberia ser nulo.");
        assertEquals(15, resultado.getIdHerramientaDTO(), "El ID generado debe coincidir.");
        assertEquals(nombreAleatorio, resultado.getNombreHerramientaDTO(), "El nombre mapeado debe ser el correcto.");
        verify(herramientaRepository, times(1)).save(any(Herramienta.class));
    }

    @Test
    void guardarHerramienta_StockInvalido() {
    //Given
        HerramientaDTO dto = new HerramientaDTO();
        dto.setNombreHerramientaDTO("Taladro");
        dto.setCantidadDisponibleDTO(-5);
    //When & Then
        RuntimeException excepcion = assertThrows(RuntimeException.class, () -> {
            herramientaService.guardarHerramienta(dto);
        });
        assertEquals("Cantidad disponible inválida", excepcion.getMessage());
        verify(herramientaRepository, times(0)).save(any(Herramienta.class));
    }

    @Test
    void guardarHerramienta_EstadoInvalido() {
    //Given
        HerramientaDTO dto = new HerramientaDTO();
        dto.setNombreHerramientaDTO("Taladro");
        dto.setEstadoHerramientaDTO("NUEVO_INVENTADO"); // Estado no listado en la validación
    //When & Then
        RuntimeException excepcion = assertThrows(RuntimeException.class, () -> {
            herramientaService.guardarHerramienta(dto);
        });
        assertEquals("Estado de herramienta inválido", excepcion.getMessage());
    }

    @Test
    void actualizarHerramienta_Exitoso() {
    //Given
        Integer idSimulado = 1;
        String nombreNuevo = faker.commerce().productName();
        String nombreViejo = faker.commerce().productName();
        
        HerramientaDTO dto = new HerramientaDTO();
        dto.setNombreHerramientaDTO(nombreNuevo);
        dto.setEstadoHerramientaDTO("EN_MANTENCIÓN");
        dto.setCantidadDisponibleDTO(3);

        Herramienta original = new Herramienta();
        original.setIdHerramienta(idSimulado);
        original.setNombreHerramienta(nombreViejo);
        original.setEstadoHerramienta("DISPONIBLE");
        original.setCantidadDisponible(5);
        original.setCantidadTotal(5);

        Herramienta actualizada = new Herramienta();
        actualizada.setIdHerramienta(idSimulado);
        actualizada.setNombreHerramienta(nombreNuevo);
        actualizada.setEstadoHerramienta("EN_MANTENCIÓN");
        actualizada.setCantidadDisponible(3);
        actualizada.setCantidadTotal(5);

        when(herramientaRepository.findById(idSimulado)).thenReturn(Optional.of(original));
        when(herramientaRepository.save(any(Herramienta.class))).thenReturn(actualizada);
    //When
        HerramientaDTO resultado = herramientaService.actualizarHerramienta(idSimulado, dto);
    //Then
        assertNotNull(resultado);
        assertEquals(nombreNuevo, resultado.getNombreHerramientaDTO(), "El nombre debería haberse actualizado");
        assertEquals("EN_MANTENCIÓN", resultado.getEstadoHerramientaDTO());
        verify(herramientaRepository, times(1)).findById(idSimulado);
        verify(herramientaRepository, times(1)).save(any(Herramienta.class));
    }

    @Test
    void actualizarHerramienta_IdNoExiste() {
    //Given
        Integer idSimulado = 99;
        HerramientaDTO dto = new HerramientaDTO();
        when(herramientaRepository.findById(idSimulado)).thenReturn(Optional.empty());
    //When & Then
        RuntimeException excepcion = assertThrows(RuntimeException.class, () -> {
            herramientaService.actualizarHerramienta(idSimulado, dto);
        });
        assertEquals("No se encontró la herramienta con el ID ingresado", excepcion.getMessage());
        verify(herramientaRepository, times(1)).findById(idSimulado);
        verify(herramientaRepository, times(0)).save(any(Herramienta.class));
    }

    @Test
    void actualizarHerramienta_StockInconsistente() {
    //Given
        Integer idSimulado = 10;
        Herramienta herramienta = new Herramienta();
        herramienta.setCantidadTotal(5);
        herramienta.setCantidadDisponible(5);
        herramienta.setNombreHerramienta("Taladro");
        HerramientaDTO dto = new HerramientaDTO();
        dto.setCantidadDisponibleDTO(20);
        when(herramientaRepository.findById(idSimulado)).thenReturn(Optional.of(herramienta));
    //When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            herramientaService.actualizarHerramienta(idSimulado, dto);
        });
        assertEquals("Cantidad disponible inválida", exception.getMessage());
        verify(herramientaRepository, times(0)).save(any(Herramienta.class));
    }

    @Test
    void eliminar_Exitoso() {
    //Given
        Integer idSimulado = 1;
        Herramienta her = new Herramienta();
        her.setIdHerramienta(idSimulado);
        her.setNombreHerramienta(faker.commerce().productName());
        when(herramientaRepository.findById(idSimulado)).thenReturn(Optional.of(her));
    //When
        String mensaje = herramientaService.eliminar(idSimulado);
    //Then
        assertEquals("La herramienta con Id '" + idSimulado + "' ha sido eliminada.", mensaje);
        verify(herramientaRepository, times(1)).findById(idSimulado);
        verify(herramientaRepository, times(1)).delete(her);
    }

    @Test
    void eliminar_IdNoExiste() {
    //Given
        Integer idSimulado = 99;
        when(herramientaRepository.findById(idSimulado)).thenReturn(Optional.empty());
    //When & Then
        RuntimeException excepcion = assertThrows(RuntimeException.class, () -> {
            herramientaService.eliminar(idSimulado);
        });
        assertEquals("No se encontró la herramienta con el ID ingresado", excepcion.getMessage());
        verify(herramientaRepository, times(1)).findById(idSimulado);
        verify(herramientaRepository, times(0)).delete(any(Herramienta.class));
    }
}

