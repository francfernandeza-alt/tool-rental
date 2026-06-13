package com.tool_rental.herramientas.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tool_rental.herramientas.DTO.HerramientaDTO;
import com.tool_rental.herramientas.DTO.MarcaDTO;
import com.tool_rental.herramientas.DTO.MaterialDTO;
import com.tool_rental.herramientas.Model.Herramienta;
import com.tool_rental.herramientas.Model.Marca;
import com.tool_rental.herramientas.Model.Material;
import com.tool_rental.herramientas.Repository.MaterialRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class MaterialService {
    @Autowired
    private MaterialRepository materialRepository;

    public List<MaterialDTO> obtenerTodos() {
        return materialRepository.findAll().stream().map(this::convertirADTO).toList();
    }

    public MaterialDTO buscarPorId(Integer id) {
        Material material = materialRepository.findById(id).orElseThrow(()-> new RuntimeException("Material no encontrado"));
        return convertirADTO(material);
    }

    public Material guardarMaterial(Material material) {
        return materialRepository.save(material);
    }

    public Material actualizarMaterial(Integer id, Material material) {
        Material materialEncontrado = materialRepository.findById(id).orElse(null);

        if (materialEncontrado != null) {
            materialEncontrado.setNombreMaterial(material.getNombreMaterial());
            materialEncontrado.setDescripcionMaterial(material.getDescripcionMaterial());
            return materialRepository.save(materialEncontrado);
        }

        return null;
    }

    public String eliminar(Integer id) {
        Material material = materialRepository.findById(id).orElse(null);

        if (material != null) {
            materialRepository.delete(material);
            return "Material eliminado correctamente";
        }

        return "Material no encontrado";
    }

    public MaterialDTO convertirADTO(Material material) {
        MaterialDTO dto = new MaterialDTO();
        dto.setIdMaterialDTO(material.getIdMaterial());
        dto.setNombreMaterialDTO(material.getNombreMaterial());
        dto.setDescripcionMaterialDTO(material.getDescripcionMaterial());
        return dto;
    }
}
