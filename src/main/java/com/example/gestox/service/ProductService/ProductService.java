package com.example.gestox.service.ProductService;

import com.example.gestox.dto.ProductRequest;
import com.example.gestox.dto.ProductResponse;
import com.example.gestox.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.IOException;
import java.util.List;

public interface ProductService {
    ProductResponse saveProduct(ProductRequest productRequest) throws IOException;
    ProductResponse updateProduct(ProductRequest productRequest);
    void deleteProduct(Long idProduct);
    ProductResponse getProductById(Long idProduct);
    List<ProductResponse> getAllProducts();

    public Page<ProductResponse> getProducts(Pageable pageable);
}

