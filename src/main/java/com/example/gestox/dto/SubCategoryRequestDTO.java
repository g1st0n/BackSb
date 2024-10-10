package com.example.gestox.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubCategoryRequestDTO {
    private String name;
    private String reference;
}
