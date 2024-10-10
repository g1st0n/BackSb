package com.example.gestox.controller;

import com.example.gestox.dto.WorkshopRequestDTO;
import com.example.gestox.dto.WorkshopResponseDTO;
import com.example.gestox.service.WorkshopService.WorkshopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/workshops")
public class WorkshopController {

    @Autowired
    private WorkshopService workshopService;


    @PostMapping("/add")
    public ResponseEntity<WorkshopResponseDTO> createWorkshop(@RequestBody WorkshopRequestDTO workshopRequestDTO) {
        WorkshopResponseDTO workshopResponse = workshopService.createWorkshop(workshopRequestDTO);
        return ResponseEntity.ok(workshopResponse);
    }

    @PutMapping("/{idWorkshop}")
    public ResponseEntity<WorkshopResponseDTO> updateWorkshop(@PathVariable Long idWorkshop, @RequestBody WorkshopRequestDTO workshopRequestDTO) {
        WorkshopResponseDTO updatedWorkshop = workshopService.updateWorkshop(idWorkshop, workshopRequestDTO);
        return ResponseEntity.ok(updatedWorkshop);
    }

    @DeleteMapping("/{idWorkshop}")
    public ResponseEntity<Void> deleteWorkshop(@PathVariable Long idWorkshop) {
        workshopService.deleteWorkshop(idWorkshop);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{idWorkshop}")
    public ResponseEntity<WorkshopResponseDTO> getWorkshopById(@PathVariable Long idWorkshop) {
        WorkshopResponseDTO workshopResponse = workshopService.getWorkshopById(idWorkshop);
        return ResponseEntity.ok(workshopResponse);
    }

    @GetMapping
    public ResponseEntity<List<WorkshopResponseDTO>> getAllWorkshops() {
        List<WorkshopResponseDTO> workshops = workshopService.getAllWorkshops();
        return ResponseEntity.ok(workshops);
    }
}

