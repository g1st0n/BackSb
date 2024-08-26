package com.example.gestox.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Parametrage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id ;
    private double cout_min ;





}
