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
public class RawMaterial {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idMaterial;

    private String name;
    private String materialType;
    private String supplier;
    private Integer availableQuantity;
    private String unit;
    private String color;
    private String origin;
    private Float unitPrice;

    @OneToMany(mappedBy = "rawMaterial")
    private List<Product> products;

}
