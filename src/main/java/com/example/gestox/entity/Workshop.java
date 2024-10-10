package com.example.gestox.entity;

import lombok.*;
import jakarta.persistence.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Workshop {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idWorkshop;

    private Integer workshopNumber;
    private Long productionCapacity;
    private Integer machineCount;
    private Float machineCost;

    @OneToMany(mappedBy = "workshop")
    private List<ProductionPlan> productionPlans;
}
