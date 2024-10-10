package com.example.gestox.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubCategoryResponseDTO {
    private Long idSubCategory;
    private String name;
    private String reference;
}
