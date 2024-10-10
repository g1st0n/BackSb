package com.example.gestox.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor

@Builder
@Entity
public class Famille_Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String designation;

    //@OneToMany(mappedBy = "categorie", cascade = CascadeType.ALL)
    //private List<Product> products;

}
