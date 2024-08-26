package com.example.gestox.dao;

import com.example.gestox.entity.Famille_Produit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FamilleRepository extends JpaRepository<Famille_Produit, Long> {
}
