package com.example.gestox.entity;

import lombok.*;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class ProductionPlan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPlanning;

    private LocalDateTime date;
    private Integer quantity;
    private LocalTime duration;

    private Integer waste;

    @ManyToOne
    @JoinColumn(name = "idProduct")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "idWorkshop")
    private Workshop workshop;

    private Integer workforce;
    private String comment;

}
