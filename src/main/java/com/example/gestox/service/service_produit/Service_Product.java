package com.example.gestox.service.service_produit;

import com.example.gestox.dao.FamilleRepository;
import com.example.gestox.dao.ProductRepository;
import com.example.gestox.dto.ProductRequest;
import com.example.gestox.dto.ProductResponse;
import com.example.gestox.entity.Famille_Product;
import com.example.gestox.entity.Product;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class Service_Product implements IService_Produit{

    private ProductRepository productRepository ;
    private FamilleRepository familleRepository ;

    @Override
    public ProductResponse getProduct(Long id) {
        ProductResponse response = new ProductResponse();
        Product product = productRepository.findById(id).orElse(null);
        response.setColor(product.getColor());
        return response;
    }

    @Override
    public Product saveProduct( ProductRequest productRequest) {
        if (productRequest.getReference() == null || productRequest.getReference().isEmpty()) {
            throw new IllegalArgumentException("Product reference cannot be empty");
        }

        if (productRequest.getSubCategory()==null) {
            throw new IllegalArgumentException("Category must be specified");
        }

        //Famille_Product category = (familleRepository.findById(productRequest.getSubCategory())).orElse(null);
        Product product = new Product();
        //product.setSubCategory(category);
        product.setReference(productRequest.getReference());
        product.setPrice(productRequest.getPrice());
        product.setProductionDuration(productRequest.getProductionDuration());
        product.setDesignation(productRequest.getDesignation());
        return productRepository.save(product);
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public void deleteProduct(Long id) {
        Optional<Product> product = productRepository.findById(id);

        product.ifPresent(p -> {
            productRepository.delete(p);
        });
    }

    @Override
    public Product editProduit(ProductRequest productRequest) throws  RuntimeException {

        if (productRequest.getId() == null) {
            throw new IllegalArgumentException("Product ID cannot be null");
        }
        if (productRequest.getReference() == null || productRequest.getReference().isEmpty()) {
            throw new IllegalArgumentException("Product reference cannot be empty");
        }
        try {
            Optional<Product> productOpt = productRepository.findById(productRequest.getId());
            Product product = null ;
            if(productOpt.isPresent()){
              product = productOpt.get();
              product.setReference(productRequest.getReference());
              product.setPrice(productRequest.getPrice());
              product.setDesignation(productRequest.getDesignation());
              product.setProductionDuration(productRequest.getProductionDuration());
              product = productRepository.save(product);
            }

            return product;
        } catch (Exception e) {
            throw new RuntimeException("Failed to update product", e);
        }
    }

}
