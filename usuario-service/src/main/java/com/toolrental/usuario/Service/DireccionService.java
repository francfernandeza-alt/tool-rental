package com.toolrental.usuario.Service;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.multiherramienta.multiherramienta.DTO.DireccionDTO;
import com.multiherramienta.multiherramienta.Model.Direccion;
import com.multiherramienta.multiherramienta.Repository.DireccionRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional

public class DireccionService {
@Autowired
    private DireccionRepository direccionRepository;

    public List<DireccionDTO> obtenerTodas() {
        return direccionRepository.findAll().stream()
                .map(this::convertirADTO)
                .toList();
    }

    public DireccionDTO buscarPorId(Integer id) {
        Direccion direccion = direccionRepository.findById(id).orElse(null);

        if (direccion != null) {
            return convertirADTO(direccion);
        }

        return null;
    }

    public Direccion guardaDireccion(Direccion direccion) {
        return direccionRepository.save(direccion);
    }

    public Direccion actualizarDireccion(Integer id, Direccion direccion) {
        Direccion direccionEncontrada = direccionRepository.findById(id).orElse(null);

        if (direccionEncontrada != null) {
            direccionEncontrada.setCalle(direccion.getCalle());
            direccionEncontrada.setNumeracion(direccion.getNumeracion());
            direccionEncontrada.setComuna(direccion.getComuna());
            direccionEncontrada.setUsuario(direccion.getUsuario());

            return direccionRepository.save(direccionEncontrada);
        }

        return null;
    }

    public String eliminar(Integer id) {
        Direccion direccion = direccionRepository.findById(id).orElse(null);

        if (direccion != null) {
            direccionRepository.delete(direccion);
            return "Dirección eliminada correctamente";
        }

        return "Dirección no encontrada";
    }


    private DireccionDTO convertirADTO(Direccion direccion) {
        DireccionDTO dto = new DireccionDTO();

        dto.setIdDireccion(direccion.getIdDireccion());
        dto.setCalle(direccion.getCalle());
        dto.setNumeracion(direccion.getNumeracion());
        
        if (direccion.getComuna() != null) {
            dto.setIdComuna(direccion.getComuna().getIdComuna());
            dto.setNombreComuna(direccion.getComuna().getNombreComuna());
        }
        if (direccion.getUsuario() != null) {
            dto.setRutUsuario(direccion.getUsuario().getRutUsuario());
            dto.setNombreUsuario(direccion.getUsuario().getNombreUsuario());
        }

        return dto;
    }
}


