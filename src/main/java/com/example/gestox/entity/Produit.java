package com.example.gestox.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Produit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String ref;
    private String designation;
    private double temps;
    private double prix;
    
    @ManyToOne
    @JoinColumn(name = "categorie_id")
    @JsonIgnoreProperties("produits")
    private Famille_Produit categorie;

}
