package com.example.gestox.dto;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderRequestDTO {

    private Long clientId;  // Reference to Client entity
    private Long productId; // Reference to Product entity
    private String date;
    private Integer quantity;
}

