package com.example.gestox.dto;

import com.example.gestox.entity.CustomerOrder;
import com.example.gestox.entity.ProductionPlan;
import com.example.gestox.entity.RawMaterial;
import com.example.gestox.entity.SubCategory;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class ProductRequest {

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
    private MultipartFile logo;

    private Long subCategory;
    private Long rawMaterial;

}
