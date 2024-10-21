package com.example.gestox.controller;

import com.example.gestox.dto.RawMaterialResponseDTO;
import com.example.gestox.dto.WorkshopRequestDTO;
import com.example.gestox.dto.WorkshopResponseDTO;
import com.example.gestox.service.WorkshopService.WorkshopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/workshops")
public class WorkshopController {

    @Autowired
    private WorkshopService workshopService;

    @GetMapping
    public Page<WorkshopResponseDTO> getAllWorkshops(Pageable pageable) {
        return workshopService.getAllWorkshops(pageable);
    }

    @PostMapping("/add")

    public ResponseEntity<WorkshopResponseDTO> createWorkshop(@RequestBody WorkshopRequestDTO workshopRequestDTO) {
        WorkshopResponseDTO workshopResponse = workshopService.createWorkshop(workshopRequestDTO);
        return ResponseEntity.ok(workshopResponse);
    }

    @PutMapping("/{idWorkshop}")
    public ResponseEntity<WorkshopResponseDTO> updateWorkshop(@RequestBody WorkshopRequestDTO workshopRequestDTO) {
        WorkshopResponseDTO updatedWorkshop = workshopService.updateWorkshop( workshopRequestDTO);
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

    @GetMapping("showAll")
    public ResponseEntity<List<WorkshopResponseDTO>> getAllWorkshops() {
        List<WorkshopResponseDTO> workshops = workshopService.getAllWorkshops();
        return ResponseEntity.ok(workshops);
    }

    @GetMapping("/generate/{workshopId}")
    public ResponseEntity<byte[]> generatePdf(@PathVariable Long workshopId) {
        try {
            byte[] pdfBytes  =workshopService.generatePdf(workshopId);

            // Return the generated PDF as a response
            HttpHeaders headers = new HttpHeaders();
            headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=ORDER_" + workshopId + ".pdf");
            headers.setContentType(MediaType.APPLICATION_PDF);

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(pdfBytes);

        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }
}

