package com.example.gestox.dto;

import lombok.*;

import java.time.LocalTime;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductionPlanRequestDTO {
    private Long planningId ;
    private String date;
    private Integer quantity;
    private Integer waste;
    private String duration;
    private Long productId;  // Reference to Product entity
    private Long workshopId; // Reference to Workshop entity
    private Integer workforce;
    private String comment;
}

