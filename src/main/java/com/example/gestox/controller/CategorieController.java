package com.example.gestox.controller;

import com.example.gestox.entity.Famille_Product;
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
    public List<Famille_Product> getAllcat(){
        return serviceCategorie.getAllCat();

    }
    @GetMapping("/cat/{id}")
    public Famille_Product getById(@PathVariable Long id){
        return serviceCategorie.getCat(id);
    }

    @PutMapping(path = "/editCat")
    public Famille_Product editcat(@RequestBody Famille_Product f){

        return serviceCategorie.editCat(f);
    }

    @DeleteMapping(path = "/deleteCat/{id}")
    public void getCat(@PathVariable Long id){
        serviceCategorie.deleteCat(id);
    }
    @PostMapping(path = "/saveCat")
    public Famille_Product SaveCat(@RequestBody Famille_Product f){
        return serviceCategorie.saveCat(f);
    }
}
