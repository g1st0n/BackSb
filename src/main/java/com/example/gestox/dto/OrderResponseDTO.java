package com.example.gestox.dto;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderResponseDTO {

    private Long idOrder;
    private String clientName;  // Reference to Client entity
    private String productName; // Reference to Product entity
    private String date;
    private Integer quantity;
}

