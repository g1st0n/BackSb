package com.example.gestox.dto;

import lombok.*;

@Getter
@Setter

public class RawMaterialRequestDTO {
    private Long idMaterial;
    private String name;
    private String materialType;
    private String supplier;
    private Integer availableQuantity;
    private String unit;
    private String color;
    private String origin;
    private Float unitPrice;
}

