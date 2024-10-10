package com.example.gestox.service.service_famille;

import com.example.gestox.entity.Famille_Product;

import java.util.List;

public interface IService_Categorie {
    public Famille_Product getCat(Long id);
    public Famille_Product saveCat(Famille_Product f);
    public List<Famille_Product> getAllCat();
    public void deleteCat(Long id);
    public Famille_Product editCat(Famille_Product f) ;
}
