package com.example.gestox.service.ProductService;

import com.example.gestox.dao.FileStorageRepository;
import com.example.gestox.dao.ProductRepository;
import com.example.gestox.dao.RawMaterialRepository;
import com.example.gestox.dao.SubCategoryRepository;
import com.example.gestox.dto.ProductRequest;
import com.example.gestox.dto.ProductResponse;
import com.example.gestox.entity.FileStorage;
import com.example.gestox.entity.Product;
import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
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
        FileStorage logo = null;
        if(productRequest.getLogo() !=null){
            if(fileStorageRepository.existsByFileNameAndFileType(productRequest.getLogo().getOriginalFilename(),
                    productRequest.getLogo().getContentType())){
                logo = fileStorageRepository.findByFileNameAndFileType(productRequest.getLogo().getOriginalFilename(),
                        productRequest.getLogo().getContentType());
                product.setLogo(logo);
            } else {
                logo = new FileStorage();
                logo.setFileName(productRequest.getLogo().getOriginalFilename());
                logo.setFileType(productRequest.getLogo().getContentType());
                logo.setData(productRequest.getLogo().getBytes());
                logo.setCreationDate(LocalDateTime.now());
                fileStorageRepository.save(logo);
                product.setLogo(logo);
            }
            product.setLogo(logo);
        }
        productRepository.save(product);
        ProductResponse response = new ProductResponse();
        response.setId(product.getIdProduct());

        return response;
    }

    @Override
    public ProductResponse updateProduct(ProductRequest productRequest) throws IOException {
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
            //updatedProduct.setRawMaterial(rawMaterialRepository.findById(productRequest.getRawMaterial()).get());
            //updatedProduct.setSubCategory(subCategoryRepository.findById(productRequest.getSubCategory()).get());
            FileStorage logo = null;
            if(productRequest.getLogo() !=null){
                if(fileStorageRepository.existsByFileNameAndFileType(productRequest.getLogo().getOriginalFilename(),
                        productRequest.getLogo().getContentType())){
                    logo = fileStorageRepository.findByFileNameAndFileType(productRequest.getLogo().getOriginalFilename(),
                            productRequest.getLogo().getContentType());
                    updatedProduct.setLogo(logo);
                } else {
                    logo = new FileStorage();
                    logo.setFileName(productRequest.getLogo().getOriginalFilename());
                    logo.setFileType(productRequest.getLogo().getContentType());
                    logo.setData(productRequest.getLogo().getBytes());
                    logo.setCreationDate(LocalDateTime.now());
                    fileStorageRepository.save(logo);
                    updatedProduct.setLogo(logo);
                }
                updatedProduct.setLogo(logo);
            }
            productRepository.save(updatedProduct);
            ProductResponse response = new ProductResponse();
            response.setId(updatedProduct.getIdProduct());
            response.setColor(updatedProduct.getColor());
            response.setDesignation(updatedProduct.getDesignation());
            response.setDimension(updatedProduct.getDimension());
            response.setPrice(updatedProduct.getPrice());
            response.setProductionCost(updatedProduct.getProductionCost());
            response.setProductionDuration(updatedProduct.getProductionDuration());
            response.setQuantity(updatedProduct.getQuantity());
            response.setReference(updatedProduct.getReference());
            response.setWeight(updatedProduct.getWeight());
            response.setRawMaterial(updatedProduct.getRawMaterial().getName());
            response.setSubCategory(updatedProduct.getSubCategory().getName());
            if (updatedProduct.getLogo() != null) {
                String logoBase64 = Base64.getEncoder().encodeToString(updatedProduct.getLogo().getData());
                response.setLogo(logoBase64);
                response.setLogoName(updatedProduct.getLogo().getFileName());
                response.setLogoType(updatedProduct.getLogo().getFileType());
            }
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
            if (product.getLogo() != null) {
                String logoBase64 = Base64.getEncoder().encodeToString(product.getLogo().getData());
                response.setLogo(logoBase64);
                response.setLogoName(product.getLogo().getFileName());
                response.setLogoType(product.getLogo().getFileType());
            }
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

    @Override
    public byte[] generatePdf(Long productId) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(byteArrayOutputStream);
        PdfDocument pdfDoc = new PdfDocument(writer);
        Document document = new Document(pdfDoc);

        // Set document font, size, and styling
        PdfFont font = PdfFontFactory.createFont(StandardFonts.HELVETICA);
        document.setFont(font);
        document.setFontSize(12);

        Optional<Product> existingProduct = productRepository.findById(productId);
        if (existingProduct.isPresent()) {
            Product product = existingProduct.get();

            // Add title in French
            Paragraph title = new Paragraph("Détails du Produit")
                    .setFontSize(18)
                    .setBold()
                    .setTextAlignment(TextAlignment.CENTER)
                    .setMarginBottom(20);
            document.add(title);

            // Product info table with null checks and French labels
            Table productInfoTable = new Table(2);
            productInfoTable.setWidth(UnitValue.createPercentValue(100));

            productInfoTable.addCell(new Cell().add(new Paragraph("Référence :")));
            productInfoTable.addCell(new Cell().add(new Paragraph(product.getReference() != null ? product.getReference() : "Non renseigné")));

            productInfoTable.addCell(new Cell().add(new Paragraph("Désignation :")));
            productInfoTable.addCell(new Cell().add(new Paragraph(product.getDesignation() != null ? product.getDesignation() : "Non renseigné")));

            productInfoTable.addCell(new Cell().add(new Paragraph("Catégorie :")));
            productInfoTable.addCell(new Cell().add(new Paragraph(product.getSubCategory() != null ? product.getSubCategory().getName() : "Non disponible")));

            productInfoTable.addCell(new Cell().add(new Paragraph("Couleur :")));
            productInfoTable.addCell(new Cell().add(new Paragraph(product.getColor() != null ? product.getColor() : "Non renseigné")));

            productInfoTable.addCell(new Cell().add(new Paragraph("Poids (g) :")));
            productInfoTable.addCell(new Cell().add(new Paragraph(product.getWeight() != null ? String.valueOf(product.getWeight()) : "Non renseigné")));

            productInfoTable.addCell(new Cell().add(new Paragraph("Dimensions (cm) :")));
            productInfoTable.addCell(new Cell().add(new Paragraph(product.getDimension() != null ? String.valueOf(product.getDimension()) : "Non renseigné")));

            productInfoTable.addCell(new Cell().add(new Paragraph("Durée de fabrication (heures) :")));
            productInfoTable.addCell(new Cell().add(new Paragraph(product.getProductionDuration() != null ? String.valueOf(product.getProductionDuration()) : "Non renseigné")));

            productInfoTable.addCell(new Cell().add(new Paragraph("Prix (TND) :")));
            productInfoTable.addCell(new Cell().add(new Paragraph(product.getPrice() != null ? String.valueOf(product.getPrice()) : "Non renseigné")));

            productInfoTable.addCell(new Cell().add(new Paragraph("Quantité en stock :")));
            productInfoTable.addCell(new Cell().add(new Paragraph(product.getQuantity() != null ? String.valueOf(product.getQuantity()) : "Non renseigné")));

            productInfoTable.addCell(new Cell().add(new Paragraph("Coût de production (TND) :")));
            productInfoTable.addCell(new Cell().add(new Paragraph(product.getProductionCost() != null ? String.valueOf(product.getProductionCost()) : "Non renseigné")));

            productInfoTable.addCell(new Cell().add(new Paragraph("Matière première :")));
            productInfoTable.addCell(new Cell().add(new Paragraph(product.getRawMaterial() != null ? product.getRawMaterial().getName() : "Non disponible")));

            document.add(productInfoTable);

            // Add logo image if available
            if (product.getLogo() != null && product.getLogo().getData() != null) {
                Image logo = new Image(ImageDataFactory.create(product.getLogo().getData()));
                logo.setWidth(100); // Adjust size
                logo.setHeight(100);
                logo.setMarginTop(20);
                document.add(logo);
            }

            // Add a final note in French
            Paragraph finalNote = new Paragraph("Ce document contient les informations détaillées du produit : " + (product.getReference() != null ? product.getReference() : "Non renseigné"))
                    .setFontSize(10)
                    .setItalic()
                    .setTextAlignment(TextAlignment.CENTER)
                    .setMarginTop(20);
            document.add(finalNote);
        } else {
            document.add(new Paragraph("Produit non trouvé").setFontSize(12).setBold());
        }

        document.close();

        // Convert ByteArrayOutputStream to byte array
        return byteArrayOutputStream.toByteArray();
    }


}
