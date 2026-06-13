package com.tool_rental.herramientas.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tool_rental.herramientas.DTO.MaterialDTO;
import com.tool_rental.herramientas.DTO.TipoHerramientaDTO;
import com.tool_rental.herramientas.Model.Material;
import com.tool_rental.herramientas.Model.TipoHerramienta;
import com.tool_rental.herramientas.Repository.TipoHerramientaRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class TipoHerramientaService {
    @Autowired
    private TipoHerramientaRepository tipoHerramientaRepository;

    public List<TipoHerramientaDTO> obtenerTodos() {
        return tipoHerramientaRepository.findAll().stream().map(this::convertirADTO).toList();
    }

    public TipoHerramientaDTO buscarPorId(Integer id) {
        TipoHerramienta tipoHerramienta = tipoHerramientaRepository.findById(id).orElseThrow(()-> new RuntimeException("Tipo de herramienta no encontrado"));
        return convertirADTO(tipoHerramienta);
    }

    public TipoHerramienta guardarTipoHerramienta(TipoHerramienta tipoHerramienta) {
        return tipoHerramientaRepository.save(tipoHerramienta);
    }

    public TipoHerramienta actualizarTipoHerramienta(Integer id, TipoHerramienta tipoHerramienta) {
        TipoHerramienta tipoHerramientaEncontrado = tipoHerramientaRepository.findById(id).orElse(null);

        if (tipoHerramientaEncontrado != null) {
            tipoHerramientaEncontrado.setNombreTipoHerramienta(tipoHerramienta.getNombreTipoHerramienta());
            tipoHerramientaEncontrado.setDescripcionTipoHerramienta(tipoHerramienta.getDescripcionTipoHerramienta());
            return tipoHerramientaRepository.save(tipoHerramientaEncontrado);
        }

        return null;
    }

    public String eliminarTipoHerramienta(Integer id) {
        TipoHerramienta tipoHerramienta = tipoHerramientaRepository.findById(id).orElse(null);

        if (tipoHerramienta != null) {
            tipoHerramientaRepository.delete(tipoHerramienta);
            return "Tipo de herramienta eliminado correctamente";
        }

        return "Tipo de herramienta no encontrado";
    }
    public TipoHerramientaDTO convertirADTO(TipoHerramienta tipoHerramienta) {
        TipoHerramientaDTO dto = new TipoHerramientaDTO();
        dto.setIdTipoHerramientaDTO(tipoHerramienta.getIdTipoHerramienta());
        dto.setNombreTipoHerramienta(tipoHerramienta.getNombreTipoHerramienta());
        dto.setDescripcionTipoHerramienta(tipoHerramienta.getDescripcionTipoHerramienta());
        return dto;
    }
}
