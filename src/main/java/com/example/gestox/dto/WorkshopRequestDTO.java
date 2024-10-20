package com.example.gestox.dto;

import lombok.*;

@Getter
@Setter

public class WorkshopRequestDTO {
    private Long idWorkshop;

    private Integer workshopNumber;
    private Long productionCapacity;
    private Integer machineCount;
    private Float machineCost;
}

