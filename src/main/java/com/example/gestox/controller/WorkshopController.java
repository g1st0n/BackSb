package com.example.gestox.controller;

import com.example.gestox.dto.ClientRequestDTO;
import com.example.gestox.dto.ClientResponseDTO;
import com.example.gestox.dto.WorkshopRequestDTO;
import com.example.gestox.dto.WorkshopResponseDTO;
import com.example.gestox.service.WorkshopService.WorkshopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/workshops")
public class WorkshopController {

    @Autowired
    private WorkshopService workshopService;


    @GetMapping
    public ResponseEntity<Page<WorkshopResponseDTO>> getAllWorkshops(Pageable pageable) {
        Page<WorkshopResponseDTO> workshops = workshopService.getAllWorkshops(pageable);
        return ResponseEntity.ok(workshops);
    }

    @PostMapping("/add")
    public ResponseEntity<WorkshopResponseDTO> createWorkshop(@RequestBody WorkshopRequestDTO workshopRequestDTO) {
        WorkshopResponseDTO workshopResponse = workshopService.createWorkshop(workshopRequestDTO);
        return ResponseEntity.ok(workshopResponse);
    }

    @PutMapping("/{id}")
    public ResponseEntity<WorkshopResponseDTO> updateClient(@RequestBody WorkshopRequestDTO workshopRequestDTO) {
        WorkshopResponseDTO workshopResponseDTO = workshopService.updateWorkshop(workshopRequestDTO);
        return ResponseEntity.ok(workshopResponseDTO);
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

    @GetMapping("/showAll")
    public ResponseEntity<List<WorkshopResponseDTO>> getAllWorkshops() {
        List<WorkshopResponseDTO> workshops = workshopService.getAllWorkshops();
        return ResponseEntity.ok(workshops);
    }
}

