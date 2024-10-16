package com.example.gestox.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class saisie_horaire {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id ;

    private Date date ;

    private double heure ;

    private long effectif ;

    private int QteProduit   ;

    private double rendener  ;
}
