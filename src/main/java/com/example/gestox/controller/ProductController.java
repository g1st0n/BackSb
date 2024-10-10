package com.example.gestox.controller;

import com.example.gestox.dto.ProductRequest;
import com.example.gestox.dto.ProductResponse;
import com.example.gestox.entity.Product;
import com.example.gestox.service.ProductService.ProductService;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    @GetMapping(path = "/showAll")
    public List<ProductResponse> getAllProduct() {
        return productService.getAllProducts();
    }

    @GetMapping
    public Page<ProductResponse> getProducts(Pageable pageable) {
        return productService.getProducts(pageable);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getById(@PathVariable Long id) {
        ProductResponse response = productService.getProductById(id);
        if(response !=null) {
            return ResponseEntity.ok(response);
        } else {
            return new ResponseEntity<>(HttpStatus.OK);
        }

    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<ProductResponse> editProduct(@RequestBody ProductRequest productRequest) {
        ProductResponse updatedProduct = productService.updateProduct(productRequest);
        return ResponseEntity.ok(updatedProduct);
    }

    @DeleteMapping(path = "/{id}")
    public void deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
    }

    @PostMapping("/add")
    public ResponseEntity<ProductResponse> saveProduct(@RequestBody ProductRequest productRequest) throws IOException {
        System.out.println("Received product: " + productRequest);

        // Pass the ProductRequest to the service to save it
        ProductResponse updatedProduct = productService.saveProduct(productRequest);
        return ResponseEntity.ok(updatedProduct);
    }
}



