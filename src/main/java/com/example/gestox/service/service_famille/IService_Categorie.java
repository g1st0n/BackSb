package com.example.gestox.service.service_famille;

import com.example.gestox.entity.Famille_Produit;
import com.example.gestox.entity.Produit;

import java.util.List;

public interface IService_Categorie {
    public Famille_Produit getCat(Long id);
    public Famille_Produit saveCat(Famille_Produit f);
    public List<Famille_Produit> getAllCat();
    public void deleteCat(Long id);
    public Famille_Produit editCat(Famille_Produit f) ;
}
