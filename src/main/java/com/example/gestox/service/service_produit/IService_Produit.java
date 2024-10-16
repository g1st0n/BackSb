package com.example.gestox.service.service_produit;

import com.example.gestox.entity.Produit;

import java.util.List;

public interface IService_Produit {

    public Produit getProduct(Long id);
    public Produit saveProduct(Produit p);
    public List<Produit> getAllProducts();
    public void deleteProduct(Long id);
    public Produit editProduit(Produit p) ;
}
