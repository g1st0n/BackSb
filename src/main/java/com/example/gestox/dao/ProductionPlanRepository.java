package com.example.gestox.dao;

import com.example.gestox.entity.ProductionPlan;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductionPlanRepository extends JpaRepository<ProductionPlan, Long> {
}
