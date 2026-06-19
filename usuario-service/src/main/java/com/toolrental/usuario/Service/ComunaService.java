package com.toolrental.usuario.Service;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import com.toolrental.usuario.Dto.ComunaDTO;
import com.toolrental.usuario.Model.Comuna;
import com.toolrental.usuario.Repository.ComunaRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class ComunaService {
 @Autowired
    private ComunaRepository comunaRepository;

    public List<ComunaDTO> obtenerTodas() {
        return comunaRepository.findAll()
                .stream()
                .map(this::convertirADTO)
                .toList();
    }

    public ComunaDTO buscarPorId(Integer id) {

        Comuna comuna = comunaRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Comuna no encontrada"));

        return convertirADTO(comuna);
    }

    public Comuna guardarComuna(Comuna comuna) {
        return comunaRepository.save(comuna);
    }

    public Comuna actualizarComuna(Integer id, Comuna comuna) {

        Comuna comunaEncontrada = comunaRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Comuna no encontrada"));

        if (comuna.getNombreComuna() != null) {
            comunaEncontrada.setNombreComuna(comuna.getNombreComuna());
        }

        if (comuna.getRegion() != null) {
            comunaEncontrada.setRegion(comuna.getRegion());
        }

        return comunaRepository.save(comunaEncontrada);
    }

    public String eliminar(Integer id) {

        Comuna comuna = comunaRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Comuna no encontrada"));

        comunaRepository.delete(comuna);

        return "Comuna eliminada correctamente";
    }

    private ComunaDTO convertirADTO(Comuna comuna) {

        ComunaDTO dto = new ComunaDTO();

        dto.setIdComuna(comuna.getIdComuna());
        dto.setNombreComuna(comuna.getNombreComuna());

        if (comuna.getRegion() != null) {
            dto.setNumeroRegion(
                    comuna.getRegion().getNumeroRegion());

            dto.setNombreRegion(
                    comuna.getRegion().getNombreRegion());
        }

        return dto;
    }
}
