package com.example.gestox.controller;

import com.example.gestox.dto.ClientResponseDTO;
import com.example.gestox.dto.RawMaterialRequestDTO;
import com.example.gestox.dto.RawMaterialResponseDTO;
import com.example.gestox.service.RawMaterialService.RawMaterialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/raw-materials")
public class RawMaterialController {

    @Autowired
    private  RawMaterialService rawMaterialService;


    @GetMapping
    public ResponseEntity<Page<RawMaterialResponseDTO>> getAllRawMaterials(Pageable pageable) {
        Page<RawMaterialResponseDTO> rawMaterials = rawMaterialService.getAllRawMaterials(pageable);
        return ResponseEntity.ok(rawMaterials);
    }

    @PostMapping("/add")
    public ResponseEntity<RawMaterialResponseDTO> createRawMaterial(@RequestBody RawMaterialRequestDTO rawMaterialRequestDTO) {
        RawMaterialResponseDTO rawMaterialResponse = rawMaterialService.createRawMaterial(rawMaterialRequestDTO);
        return ResponseEntity.ok(rawMaterialResponse);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RawMaterialResponseDTO> updateRawMaterial(@RequestBody RawMaterialRequestDTO rawMaterialRequestDTO) {
        RawMaterialResponseDTO updatedRawMaterial = rawMaterialService.updateRawMaterial(rawMaterialRequestDTO);
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

    @GetMapping("/showAll")
    public ResponseEntity<List<RawMaterialResponseDTO>> getAllRawMaterials() {
        List<RawMaterialResponseDTO> rawMaterials = rawMaterialService.getAllRawMaterials();
        return ResponseEntity.ok(rawMaterials);
    }
}

