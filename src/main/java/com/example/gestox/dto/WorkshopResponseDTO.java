package com.example.gestox.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WorkshopResponseDTO {
    private Long idWorkshop;
    private Integer workshopNumber;
    private Long productionCapacity;
    private Integer machineCount;
    private Float machineCost;
}

