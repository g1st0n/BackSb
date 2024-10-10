package com.example.gestox.controller;

import com.example.gestox.dto.ProductionPlanRequestDTO;
import com.example.gestox.dto.ProductionPlanResponseDTO;
import com.example.gestox.service.ProductionPlanService.ProductionPlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/production-plans")
public class ProductionPlanController {

    @Autowired
    private ProductionPlanService productionPlanService;

    @PostMapping("/add")
    public ResponseEntity<ProductionPlanResponseDTO> createProductionPlan(@RequestBody ProductionPlanRequestDTO productionPlanRequestDTO) {
        ProductionPlanResponseDTO productionPlanResponse = productionPlanService.createProductionPlan(productionPlanRequestDTO);
        return ResponseEntity.ok(productionPlanResponse);
    }

    @PutMapping("/{idPlanning}")
    public ResponseEntity<ProductionPlanResponseDTO> updateProductionPlan(@PathVariable Long idPlanning, @RequestBody ProductionPlanRequestDTO productionPlanRequestDTO) {
        ProductionPlanResponseDTO updatedProductionPlan = productionPlanService.updateProductionPlan(idPlanning, productionPlanRequestDTO);
        return ResponseEntity.ok(updatedProductionPlan);
    }

    @DeleteMapping("/{idPlanning}")
    public ResponseEntity<Void> deleteProductionPlan(@PathVariable Long idPlanning) {
        productionPlanService.deleteProductionPlan(idPlanning);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{idPlanning}")
    public ResponseEntity<ProductionPlanResponseDTO> getProductionPlanById(@PathVariable Long idPlanning) {
        ProductionPlanResponseDTO productionPlanResponse = productionPlanService.getProductionPlanById(idPlanning);
        return ResponseEntity.ok(productionPlanResponse);
    }

    @GetMapping
    public ResponseEntity<List<ProductionPlanResponseDTO>> getAllProductionPlans() {
        List<ProductionPlanResponseDTO> productionPlans = productionPlanService.getAllProductionPlans();
        return ResponseEntity.ok(productionPlans);
    }
}

