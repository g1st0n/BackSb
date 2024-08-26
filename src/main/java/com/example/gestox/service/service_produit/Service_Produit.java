package com.example.gestox.service.service_produit;

import com.example.gestox.dao.ProduitRepository;
import com.example.gestox.entity.Produit;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class Service_Produit implements IService_Produit{

    private ProduitRepository produitRepository ;

    @Override
    public Produit getProduct(Long id) {
        return produitRepository.findById(id).orElse(null);
    }

    @Override
    public Produit saveProduct(Produit p) {
        if (p.getRef() == null || p.getRef().isEmpty()) {
            throw new IllegalArgumentException("Product ref cannot be empty");
        }
        return produitRepository.save(p);
    }

    @Override
    public List<Produit> getAllProducts() {
        return produitRepository.findAll();
    }

    @Override
    public void deleteProduct(Long id) {
        Optional<Produit> product = produitRepository.findById(id);

        product.ifPresent(p -> {
            produitRepository.delete(p);
        });
    }

    @Override
    public Produit editProduit(Produit p) throws  RuntimeException {

        if (p.getId() == null) {
            throw new IllegalArgumentException("Product ID cannot be null");
        }
        if (p.getRef() == null || p.getRef().isEmpty()) {
            throw new IllegalArgumentException("Product ref cannot be empty");
        }
        try {
            return produitRepository.save(p);
        } catch (Exception e) {
            throw new RuntimeException("Failed to update product", e);
        }
    }

}
