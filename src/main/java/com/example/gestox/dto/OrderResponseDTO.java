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
    private Long clientId;  // Reference to Client entity
    private Long productId; // Reference to Product entity
    private Date date;
    private Integer quantity;
}

