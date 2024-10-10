package com.example.gestox.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idProduct;

    private String reference;
    private String designation;
    private String color;
    private Float weight;
    private Float dimension;
    private Integer productionDuration;
    private Float price;
    private Integer quantity;
    private Float productionCost;

    @ManyToOne
    @JoinColumn(name = "idSubCategory")
    private SubCategory subCategory;

    @OneToMany(mappedBy = "product")
    private List<Order> orders;

    @OneToMany(mappedBy = "product")
    private List<ProductionPlan> productionPlans;

    @ManyToOne
    @JoinColumn(name = "idRawMaterial")
    private RawMaterial rawMaterial;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "image_id")
    private FileStorage logo;

}
