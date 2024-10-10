package com.example.gestox.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class ProductResponse {

    private Long id;

    private String reference;
    private String designation;
    private String color;
    private Float weight;
    private Float dimension;
    private Integer productionDuration;
    private Float price;
    private Integer quantity;
    private Float productionCost;
    private String logo;
    private String logoName;
    private String logoType;

    private String subCategory;
    private String rawMaterial;

}
