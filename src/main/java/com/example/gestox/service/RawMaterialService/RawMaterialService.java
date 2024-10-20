package com.example.gestox.service.RawMaterialService;


import com.example.gestox.dto.ClientResponseDTO;
import com.example.gestox.dto.RawMaterialRequestDTO;
import com.example.gestox.dto.RawMaterialResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.IOException;
import java.util.List;

public interface RawMaterialService {
    RawMaterialResponseDTO createRawMaterial(RawMaterialRequestDTO rawMaterialRequestDTO);
    RawMaterialResponseDTO updateRawMaterial(RawMaterialRequestDTO rawMaterialRequestDTO);
    void deleteRawMaterial(Long idMaterial);
    RawMaterialResponseDTO getRawMaterialById(Long idMaterial);
    List<RawMaterialResponseDTO> getAllRawMaterials();
    public Page<RawMaterialResponseDTO> getAllRawMaterials(Pageable pageable);


    public byte[] generatePdf(Long id) throws IOException;

}

