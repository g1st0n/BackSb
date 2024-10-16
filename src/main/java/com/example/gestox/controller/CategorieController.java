package com.example.gestox.controller;

import com.example.gestox.entity.Famille_Produit;
import com.example.gestox.entity.Produit;
import com.example.gestox.service.service_famille.IService_Categorie;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@AllArgsConstructor
public class CategorieController {
    private IService_Categorie serviceCategorie ;


    @GetMapping(path = "/cats")
    public List<Famille_Produit> getAllcat(){
        return serviceCategorie.getAllCat();

    }
    @GetMapping("/cat/{id}")
    public Famille_Produit getById(@PathVariable Long id){
        return serviceCategorie.getCat(id);
    }

    @PutMapping(path = "/editCat")
    public Famille_Produit editcat(@RequestBody Famille_Produit f){

        return serviceCategorie.editCat(f);
    }

    @DeleteMapping(path = "/deleteCat/{id}")
    public void getCat(@PathVariable Long id){
        serviceCategorie.deleteCat(id);
    }
    @PostMapping(path = "/saveCat")
    public Famille_Produit SaveCat(@RequestBody Famille_Produit f){
        return serviceCategorie.saveCat(f);
    }
}
