package com.example.gestox.controller;

import com.example.gestox.entity.Client;
import com.example.gestox.entity.Famille_Produit;
import com.example.gestox.entity.Produit;
import com.example.gestox.service.service_famille.IService_Categorie;
import com.example.gestox.service.service_produit.IService_Produit;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@AllArgsConstructor
public class ProduitController {

    private final IService_Produit serviceProduit;
    private final IService_Categorie serviceCategorie;

    @GetMapping(path = "/produits")
    public List<Produit> getAllProduct() {
        return serviceProduit.getAllProducts();
    }

    @GetMapping("/produit/{id}")
    public Produit getById(@PathVariable Long id) {
        return serviceProduit.getProduct(id);
    }

    @PutMapping(path = "/editProd")
    public Produit editProduit(@RequestBody Produit p) {
        return serviceProduit.editProduit(p);
    }

    @PutMapping(path = "/{catId}/produit/{produitId}")
    public Produit addProdToCat(@RequestBody Produit p, @PathVariable Long catId,
                                @PathVariable Long produitId) {
        Famille_Produit cat = serviceCategorie.getCat(catId);
        Produit prod = serviceProduit.getProduct(produitId);
        prod.setCategorie(cat);
        return serviceProduit.saveProduct(prod);
    }

    @DeleteMapping(path = "/delete/{id}")
    public void deleteProduit(@PathVariable Long id) {
        serviceProduit.deleteProduct(id);
    }

    @PostMapping(path = "/save")
    public Produit saveProduct(@RequestBody Produit p) {
        System.out.println("Received product: " + p);

        Famille_Produit categorie = p.getCategorie();

        if (categorie == null || categorie.getId() == null) {
            throw new IllegalArgumentException("Category must be specified");
        }

        Long categoryId = categorie.getId(); // Access the categorie_id through the associated categorie object
        System.out.println("Received ID: " + categoryId);
        Famille_Produit existingCategorie = serviceCategorie.getCat(categoryId);
        if (existingCategorie == null) {
            throw new IllegalArgumentException("Category with ID " + categoryId + " not found");
        }

        p.setCategorie(existingCategorie);
        return serviceProduit.saveProduct(p);
    }
}



