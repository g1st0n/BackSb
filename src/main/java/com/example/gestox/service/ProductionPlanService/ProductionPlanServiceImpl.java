package com.example.gestox.service.ProductionPlanService;

import com.example.gestox.dao.ProductRepository;
import com.example.gestox.dao.ProductionPlanRepository;
import com.example.gestox.dao.WorkshopRepository;
import com.example.gestox.dto.ProductionPlanRequestDTO;
import com.example.gestox.dto.ProductionPlanResponseDTO;
import com.example.gestox.entity.Product;
import com.example.gestox.entity.ProductionPlan;
import com.example.gestox.entity.Workshop;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductionPlanServiceImpl implements ProductionPlanService {

    @Autowired
    private ProductionPlanRepository productionPlanRepository;

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private WorkshopRepository workshopRepository;

    @Override
    public ProductionPlanResponseDTO createProductionPlan(ProductionPlanRequestDTO productionPlanRequestDTO) {
        Product product = productRepository.findById(productionPlanRequestDTO.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + productionPlanRequestDTO.getProductId()));

        Workshop workshop = workshopRepository.findById(productionPlanRequestDTO.getWorkshopId())
                .orElseThrow(() -> new RuntimeException("Workshop not found with id: " + productionPlanRequestDTO.getWorkshopId()));

        ProductionPlan productionPlan = mapToEntity(productionPlanRequestDTO, product, workshop);
        ProductionPlan savedProductionPlan = productionPlanRepository.save(productionPlan);
        return mapToResponseDTO(savedProductionPlan);
    }

    @Override
    public ProductionPlanResponseDTO updateProductionPlan(Long idPlanning, ProductionPlanRequestDTO productionPlanRequestDTO) {
        ProductionPlan productionPlan = productionPlanRepository.findById(idPlanning)
                .orElseThrow(() -> new RuntimeException("ProductionPlan not found with id: " + idPlanning));

        Product product = productRepository.findById(productionPlanRequestDTO.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + productionPlanRequestDTO.getProductId()));

        Workshop workshop = workshopRepository.findById(productionPlanRequestDTO.getWorkshopId())
                .orElseThrow(() -> new RuntimeException("Workshop not found with id: " + productionPlanRequestDTO.getWorkshopId()));

        // Update production plan details
        productionPlan.setDate(productionPlanRequestDTO.getDate());
        productionPlan.setQuantity(productionPlanRequestDTO.getQuantity());
        productionPlan.setDuration(productionPlanRequestDTO.getDuration());
        productionPlan.setProduct(product);
        productionPlan.setWorkshop(workshop);
        productionPlan.setWorkforce(productionPlanRequestDTO.getWorkforce());
        productionPlan.setComment(productionPlanRequestDTO.getComment());

        ProductionPlan updatedProductionPlan = productionPlanRepository.save(productionPlan);
        return mapToResponseDTO(updatedProductionPlan);
    }

    @Override
    public void deleteProductionPlan(Long idPlanning) {
        if (productionPlanRepository.existsById(idPlanning)) {
            productionPlanRepository.deleteById(idPlanning);
        } else {
            throw new RuntimeException("ProductionPlan not found with id: " + idPlanning);
        }
    }

    @Override
    public ProductionPlanResponseDTO getProductionPlanById(Long idPlanning) {
        ProductionPlan productionPlan = productionPlanRepository.findById(idPlanning)
                .orElseThrow(() -> new RuntimeException("ProductionPlan not found with id: " + idPlanning));
        return mapToResponseDTO(productionPlan);
    }

    @Override
    public List<ProductionPlanResponseDTO> getAllProductionPlans() {
        List<ProductionPlan> productionPlans = productionPlanRepository.findAll();
        return productionPlans.stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    private ProductionPlan mapToEntity(ProductionPlanRequestDTO productionPlanRequestDTO, Product product, Workshop workshop) {
        return ProductionPlan.builder()
                .date(productionPlanRequestDTO.getDate())
                .quantity(productionPlanRequestDTO.getQuantity())
                .duration(productionPlanRequestDTO.getDuration())
                .product(product)
                .workshop(workshop)
                .workforce(productionPlanRequestDTO.getWorkforce())
                .comment(productionPlanRequestDTO.getComment())
                .build();
    }

    private ProductionPlanResponseDTO mapToResponseDTO(ProductionPlan productionPlan) {
        return ProductionPlanResponseDTO.builder()
                .idPlanning(productionPlan.getIdPlanning())
                .date(productionPlan.getDate())
                .quantity(productionPlan.getQuantity())
                .duration(productionPlan.getDuration())
                .productId(productionPlan.getProduct().getIdProduct())
                .workshopId(productionPlan.getWorkshop().getIdWorkshop())
                .workforce(productionPlan.getWorkforce())
                .comment(productionPlan.getComment())
                .build();
    }
}