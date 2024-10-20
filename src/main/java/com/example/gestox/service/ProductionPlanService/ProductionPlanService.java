package com.example.gestox.service.ProductionPlanService;

import com.example.gestox.dto.ProductionPlanRequestDTO;
import com.example.gestox.dto.ProductionPlanResponseDTO;
import com.example.gestox.entity.ProductionPlan;

import java.io.IOException;
import java.util.List;

public interface ProductionPlanService {
    ProductionPlanResponseDTO createProductionPlan(ProductionPlanRequestDTO productionPlanRequestDTO);
    ProductionPlanResponseDTO updateProductionPlan(Long idPlanning, ProductionPlanRequestDTO productionPlanRequestDTO);
    void deleteProductionPlan(Long idPlanning);
    ProductionPlanResponseDTO getProductionPlanById(Long idPlanning);
    List<ProductionPlanResponseDTO> getAllProductionPlans();

    public byte[] generatePdf(Long id) throws IOException;
}
