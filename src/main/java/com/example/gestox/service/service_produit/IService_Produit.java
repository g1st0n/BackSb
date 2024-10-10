package com.example.gestox.service.service_produit;

import com.example.gestox.dto.ProductRequest;
import com.example.gestox.dto.ProductResponse;
import com.example.gestox.entity.Product;

import java.util.List;

public interface IService_Produit {

    public ProductResponse getProduct(Long id);
    public Product saveProduct(ProductRequest productRequest);
    public List<Product> getAllProducts();
    public void deleteProduct(Long id);
    public Product editProduit(ProductRequest productRequest) ;
}
