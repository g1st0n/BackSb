package com.example.gestox.dto;

import lombok.*;

import java.time.LocalTime;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductionPlanResponseDTO {
    private Long idPlanning;
    private String date;
    private Integer quantity;
    private Integer waste;
    private String duration;
    private Long productId;  // Reference to Product entity
    private Long workshopId; // Reference to Workshop entity
    private Integer workforce;
    private String comment;
    private String status;
    private String logo;
    private String logoName;
    private String logoType;
}

