package com.example.gestox.controller;

import com.example.gestox.dto.RawMaterialRequestDTO;
import com.example.gestox.dto.RawMaterialResponseDTO;
import com.example.gestox.service.RawMaterialService.RawMaterialService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/raw-materials")
public class RawMaterialController {

    private final RawMaterialService rawMaterialService;

    public RawMaterialController(RawMaterialService rawMaterialService) {
        this.rawMaterialService = rawMaterialService;
    }

    @PostMapping("/add")
    public ResponseEntity<RawMaterialResponseDTO> createRawMaterial(@ModelAttribute RawMaterialRequestDTO rawMaterialRequestDTO) {
        RawMaterialResponseDTO rawMaterialResponse = rawMaterialService.createRawMaterial(rawMaterialRequestDTO);
        return ResponseEntity.ok(rawMaterialResponse);
    }

    @PutMapping("/{idMaterial}")
    public ResponseEntity<RawMaterialResponseDTO> updateRawMaterial(@PathVariable Long idMaterial, @RequestBody RawMaterialRequestDTO rawMaterialRequestDTO) {
        RawMaterialResponseDTO updatedRawMaterial = rawMaterialService.updateRawMaterial(idMaterial, rawMaterialRequestDTO);
        return ResponseEntity.ok(updatedRawMaterial);
    }

    @DeleteMapping("/{idMaterial}")
    public ResponseEntity<Void> deleteRawMaterial(@PathVariable Long idMaterial) {
        rawMaterialService.deleteRawMaterial(idMaterial);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{idMaterial}")
    public ResponseEntity<RawMaterialResponseDTO> getRawMaterialById(@PathVariable Long idMaterial) {
        RawMaterialResponseDTO rawMaterialResponse = rawMaterialService.getRawMaterialById(idMaterial);
        return ResponseEntity.ok(rawMaterialResponse);
    }

    @GetMapping
    public ResponseEntity<List<RawMaterialResponseDTO>> getAllRawMaterials() {
        List<RawMaterialResponseDTO> rawMaterials = rawMaterialService.getAllRawMaterials();
        return ResponseEntity.ok(rawMaterials);
    }
}

