package com.example.gestox.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class SubCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idSubCategory;

    private String name;
    private String reference;

    @OneToMany(mappedBy = "subCategory")
    private List<Product> products;

}

