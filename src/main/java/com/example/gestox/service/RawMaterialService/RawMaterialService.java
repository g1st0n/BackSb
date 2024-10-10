package com.example.gestox.service.RawMaterialService;


import com.example.gestox.dto.RawMaterialRequestDTO;
import com.example.gestox.dto.RawMaterialResponseDTO;

import java.util.List;

public interface RawMaterialService {
    RawMaterialResponseDTO createRawMaterial(RawMaterialRequestDTO rawMaterialRequestDTO);
    RawMaterialResponseDTO updateRawMaterial(Long idMaterial, RawMaterialRequestDTO rawMaterialRequestDTO);
    void deleteRawMaterial(Long idMaterial);
    RawMaterialResponseDTO getRawMaterialById(Long idMaterial);
    List<RawMaterialResponseDTO> getAllRawMaterials();

}

