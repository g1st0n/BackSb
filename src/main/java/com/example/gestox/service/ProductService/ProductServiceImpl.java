package com.example.gestox.service.ProductService;

import com.example.gestox.dao.FileStorageRepository;
import com.example.gestox.dao.ProductRepository;
import com.example.gestox.dao.RawMaterialRepository;
import com.example.gestox.dao.SubCategoryRepository;
import com.example.gestox.dto.ProductRequest;
import com.example.gestox.dto.ProductResponse;
import com.example.gestox.entity.FileStorage;
import com.example.gestox.entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private RawMaterialRepository rawMaterialRepository;

    @Autowired
    private SubCategoryRepository subCategoryRepository;

    @Autowired
    private FileStorageRepository fileStorageRepository;

    @Override
    public ProductResponse saveProduct(ProductRequest productRequest) throws IOException {
        Product product = new Product();
        product.setColor(productRequest.getColor());
        product.setDesignation(productRequest.getDesignation());
        product.setDimension(productRequest.getDimension());
        product.setPrice(productRequest.getPrice());
        product.setProductionCost(productRequest.getProductionCost());
        product.setProductionDuration(productRequest.getProductionDuration());
        product.setQuantity(productRequest.getQuantity());
        product.setReference(productRequest.getReference());
        product.setWeight(productRequest.getWeight());
        //product.setRawMaterial(rawMaterialRepository.findById(productRequest.getRawMaterial()).get());
        //product.setSubCategory(subCategoryRepository.findById(productRequest.getSubCategory()).get());
//        FileStorage logo = null;
//        if(productRequest.getLogo() !=null){
//            if(fileStorageRepository.existsByFileNameAndFileType(productRequest.getLogo().getOriginalFilename(),
//                    productRequest.getLogo().getContentType())){
//                logo = fileStorageRepository.findByFileNameAndFileType(productRequest.getLogo().getOriginalFilename(),
//                        productRequest.getLogo().getContentType());
//                product.setLogo(logo);
//            } else {
//                logo = new FileStorage();
//                logo.setFileName(productRequest.getLogo().getOriginalFilename());
//                logo.setFileType(productRequest.getLogo().getContentType());
//                logo.setData(productRequest.getLogo().getBytes());
//                logo.setCreationDate(LocalDateTime.now());
//                fileStorageRepository.save(logo);
//                product.setLogo(logo);
//            }
//            product.setLogo(logo);
//        }
        productRepository.save(product);
        ProductResponse response = new ProductResponse();
        response.setId(product.getIdProduct());

        return response;
    }

    @Override
    public ProductResponse updateProduct(ProductRequest productRequest) {
        Optional<Product> existingProduct = productRepository.findById(productRequest.getId());
        if (existingProduct.isPresent()) {
            Product updatedProduct = existingProduct.get();
            updatedProduct.setColor(productRequest.getColor());
            updatedProduct.setDesignation(productRequest.getDesignation());
            updatedProduct.setDimension(productRequest.getDimension());
            updatedProduct.setPrice(productRequest.getPrice());
            updatedProduct.setProductionCost(productRequest.getProductionCost());
            updatedProduct.setProductionDuration(productRequest.getProductionDuration());
            updatedProduct.setQuantity(productRequest.getQuantity());
            updatedProduct.setReference(productRequest.getReference());
            updatedProduct.setWeight(productRequest.getWeight());
            updatedProduct.setRawMaterial(rawMaterialRepository.findById(productRequest.getRawMaterial()).get());
            updatedProduct.setSubCategory(subCategoryRepository.findById(productRequest.getSubCategory()).get());
            productRepository.save(updatedProduct);
            ProductResponse response = new ProductResponse();
            response.setId(updatedProduct.getIdProduct());
            return response ;
        }
        return null;
    }

    @Override
    public void deleteProduct(Long idProduct) {
        productRepository.deleteById(idProduct);
    }

    @Override
    public ProductResponse getProductById(Long idProduct) {
        Optional<Product> existingProduct = productRepository.findById(idProduct);
        if(existingProduct.isPresent()) {
            ProductResponse response = new ProductResponse();
            Product product = existingProduct.get();
            response.setId(product.getIdProduct());
            response.setColor(product.getColor());
            response.setDesignation(product.getDesignation());
            response.setDimension(product.getDimension());
            response.setPrice(product.getPrice());
            response.setProductionCost(product.getProductionCost());
            response.setProductionDuration(product.getProductionDuration());
            response.setQuantity(product.getQuantity());
            response.setReference(product.getReference());
            response.setWeight(product.getWeight());
            response.setRawMaterial(product.getRawMaterial().getName());
            response.setSubCategory(product.getSubCategory().getName());
            return response;
        } else {
            return null;
        }

    }

    @Override
    public List<ProductResponse> getAllProducts() {
        List<Product> products = productRepository.findAll();
        List<ProductResponse> productResponseDTOs = products.stream().map(product -> {
            ProductResponse response = new ProductResponse();
            response.setId(product.getIdProduct());
            response.setColor(product.getColor());
            response.setDesignation(product.getDesignation());
            response.setDimension(product.getDimension());
            response.setPrice(product.getPrice());
            response.setProductionCost(product.getProductionCost());
            response.setProductionDuration(product.getProductionDuration());
            response.setQuantity(product.getQuantity());
            response.setReference(product.getReference());
            response.setWeight(product.getWeight());
            response.setRawMaterial(product.getRawMaterial()!=null?product.getRawMaterial().getName():"");
            response.setSubCategory(product.getSubCategory()!=null?product.getSubCategory().getName():"");
            if (product.getLogo() != null) {
                String logoBase64 = Base64.getEncoder().encodeToString(product.getLogo().getData());
                response.setLogo(logoBase64);
                response.setLogoName(product.getLogo().getFileName());
                response.setLogoType(product.getLogo().getFileType());
            }
            return response;
        }).collect(Collectors.toList());
        return productResponseDTOs;
    }

    @Override
    public Page<ProductResponse> getProducts(Pageable pageable) {
        Page<Product> products = productRepository.findAll(pageable);
        List<ProductResponse> productResponseDTOs = products.stream().map(product -> {
            ProductResponse response = new ProductResponse();
            response.setId(product.getIdProduct());
            response.setColor(product.getColor());
            response.setDesignation(product.getDesignation());
            response.setDimension(product.getDimension());
            response.setPrice(product.getPrice());
            response.setProductionCost(product.getProductionCost());
            response.setProductionDuration(product.getProductionDuration());
            response.setQuantity(product.getQuantity());
            response.setReference(product.getReference());
            response.setWeight(product.getWeight());
            response.setRawMaterial(product.getRawMaterial() != null ? product.getRawMaterial().getName() : "");
            response.setSubCategory(product.getSubCategory() != null ? product.getSubCategory().getName() : "");

            // Handling logo conversion to Base64
            if (product.getLogo() != null) {
                String logoBase64 = Base64.getEncoder().encodeToString(product.getLogo().getData());
                response.setLogo(logoBase64);  // This will hold the Base64-encoded image
                response.setLogoType(product.getLogo().getFileType());  // Set the MIME type (e.g., 'image/jpeg')
                response.setLogoName(product.getLogo().getFileName());
            }

            return response;
        }).collect(Collectors.toList());

        // Return a new PageImpl<ProductResponse> to preserve pagination info
        return new PageImpl<>(productResponseDTOs, pageable, products.getTotalElements());
    }
}
