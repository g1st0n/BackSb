package com.example.gestox.service.RawMaterialService;

import com.example.gestox.dao.RawMaterialRepository;
import com.example.gestox.dto.RawMaterialRequestDTO;
import com.example.gestox.dto.RawMaterialResponseDTO;
import com.example.gestox.entity.RawMaterial;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RawMaterialServiceImpl implements RawMaterialService {

    @Autowired
    private RawMaterialRepository rawMaterialRepository;

    @Override
    public RawMaterialResponseDTO createRawMaterial(RawMaterialRequestDTO rawMaterialRequestDTO) {
        RawMaterial rawMaterial = mapToEntity(rawMaterialRequestDTO);
        RawMaterial savedRawMaterial = rawMaterialRepository.save(rawMaterial);
        return mapToResponseDTO(savedRawMaterial);
    }

    @Override
    public RawMaterialResponseDTO updateRawMaterial(Long idMaterial, RawMaterialRequestDTO rawMaterialRequestDTO) {
        RawMaterial rawMaterial = rawMaterialRepository.findById(idMaterial)
                .orElseThrow(() -> new RuntimeException("Raw material not found with id: " + idMaterial));

        // Update raw material details
        rawMaterial.setName(rawMaterialRequestDTO.getName());
        rawMaterial.setMaterialType(rawMaterialRequestDTO.getMaterialType());
        rawMaterial.setSupplier(rawMaterialRequestDTO.getSupplier());
        rawMaterial.setAvailableQuantity(rawMaterialRequestDTO.getAvailableQuantity());
        rawMaterial.setUnit(rawMaterialRequestDTO.getUnit());
        rawMaterial.setColor(rawMaterialRequestDTO.getColor());
        rawMaterial.setOrigin(rawMaterialRequestDTO.getOrigin());
        rawMaterial.setUnitPrice(rawMaterialRequestDTO.getUnitPrice());

        RawMaterial updatedRawMaterial = rawMaterialRepository.save(rawMaterial);
        return mapToResponseDTO(updatedRawMaterial);
    }

    @Override
    public void deleteRawMaterial(Long idMaterial) {
        if (rawMaterialRepository.existsById(idMaterial)) {
            rawMaterialRepository.deleteById(idMaterial);
        } else {
            throw new RuntimeException("Raw material not found with id: " + idMaterial);
        }
    }

    @Override
    public RawMaterialResponseDTO getRawMaterialById(Long idMaterial) {
        RawMaterial rawMaterial = rawMaterialRepository.findById(idMaterial)
                .orElseThrow(() -> new RuntimeException("Raw material not found with id: " + idMaterial));
        return mapToResponseDTO(rawMaterial);
    }

    @Override
    public List<RawMaterialResponseDTO> getAllRawMaterials() {
        List<RawMaterial> rawMaterials = rawMaterialRepository.findAll();
        return rawMaterials.stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    private RawMaterial mapToEntity(RawMaterialRequestDTO rawMaterialRequestDTO) {
        return RawMaterial.builder()
                .name(rawMaterialRequestDTO.getName())
                .materialType(rawMaterialRequestDTO.getMaterialType())
                .supplier(rawMaterialRequestDTO.getSupplier())
                .availableQuantity(rawMaterialRequestDTO.getAvailableQuantity())
                .unit(rawMaterialRequestDTO.getUnit())
                .color(rawMaterialRequestDTO.getColor())
                .origin(rawMaterialRequestDTO.getOrigin())
                .unitPrice(rawMaterialRequestDTO.getUnitPrice())
                .build();
    }

    private RawMaterialResponseDTO mapToResponseDTO(RawMaterial rawMaterial) {
        return RawMaterialResponseDTO.builder()
                .idMaterial(rawMaterial.getIdMaterial())
                .name(rawMaterial.getName())
                .materialType(rawMaterial.getMaterialType())
                .supplier(rawMaterial.getSupplier())
                .availableQuantity(rawMaterial.getAvailableQuantity())
                .unit(rawMaterial.getUnit())
                .color(rawMaterial.getColor())
                .origin(rawMaterial.getOrigin())
                .unitPrice(rawMaterial.getUnitPrice())
                .build();
    }
}