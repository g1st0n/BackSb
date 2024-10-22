package com.example.gestox.service.ProductionPlanService;

import com.example.gestox.dao.ProductRepository;
import com.example.gestox.dao.ProductionPlanRepository;
import com.example.gestox.dao.WorkshopRepository;
import com.example.gestox.dto.ProductionPlanRequestDTO;
import com.example.gestox.dto.ProductionPlanResponseDTO;
import com.example.gestox.entity.*;
import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductionPlanServiceImpl implements ProductionPlanService {

    @Autowired
    private ProductionPlanRepository productionPlanRepository;

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private WorkshopRepository workshopRepository;

    @Override
    public ProductionPlanResponseDTO createProductionPlan(ProductionPlanRequestDTO productionPlanRequestDTO) {
        Product product = productRepository.findById(productionPlanRequestDTO.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + productionPlanRequestDTO.getProductId()));

        Workshop workshop = workshopRepository.findById(productionPlanRequestDTO.getWorkshopId())
                .orElseThrow(() -> new RuntimeException("Workshop not found with id: " + productionPlanRequestDTO.getWorkshopId()));

        ProductionPlan productionPlan = mapToEntity(productionPlanRequestDTO, product, workshop);
        ProductionPlan savedProductionPlan = productionPlanRepository.save(productionPlan);
        return mapToResponseDTO(savedProductionPlan);
    }

    @Override
    public ProductionPlanResponseDTO updateProductionPlan(Long idPlanning, ProductionPlanRequestDTO productionPlanRequestDTO) {
        ProductionPlan productionPlan = productionPlanRepository.findById(idPlanning)
                .orElseThrow(() -> new RuntimeException("ProductionPlan not found with id: " + idPlanning));

        Product product = productRepository.findById(productionPlanRequestDTO.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + productionPlanRequestDTO.getProductId()));

        Workshop workshop = workshopRepository.findById(productionPlanRequestDTO.getWorkshopId())
                .orElseThrow(() -> new RuntimeException("Workshop not found with id: " + productionPlanRequestDTO.getWorkshopId()));

        // Update production plan details
        productionPlan.setDate(getDate(productionPlanRequestDTO.getDate()));
        productionPlan.setQuantity(productionPlanRequestDTO.getQuantity());
        //productionPlan.setDuration(productionPlanRequestDTO.getDuration());
        productionPlan.setProduct(product);
        productionPlan.setWorkshop(workshop);
        productionPlan.setWorkforce(productionPlanRequestDTO.getWorkforce());
        productionPlan.setComment(productionPlanRequestDTO.getComment());

        ProductionPlan updatedProductionPlan = productionPlanRepository.save(productionPlan);
        return mapToResponseDTO(updatedProductionPlan);
    }

    @Override
    public ProductionPlanResponseDTO confirmProductionPlan(Long idPlanning, ProductionPlanRequestDTO productionPlanRequestDTO) {
        ProductionPlan productionPlan = productionPlanRepository.findById(idPlanning)
                .orElseThrow(() -> new RuntimeException("ProductionPlan not found with id: " + idPlanning));

        // Update production plan details
        productionPlan.setWaste(productionPlanRequestDTO.getWaste()!=null?productionPlanRequestDTO.getWaste() : 0);

        ProductionPlan updatedProductionPlan = productionPlanRepository.save(productionPlan);
        return mapToResponseDTO(updatedProductionPlan);
    }

    @Override
    public void deleteProductionPlan(Long idPlanning) {
        if (productionPlanRepository.existsById(idPlanning)) {
            productionPlanRepository.deleteById(idPlanning);
        } else {
            throw new RuntimeException("ProductionPlan not found with id: " + idPlanning);
        }
    }

    @Override
    public ProductionPlanResponseDTO getProductionPlanById(Long idPlanning) {
        ProductionPlan productionPlan = productionPlanRepository.findById(idPlanning)
                .orElseThrow(() -> new RuntimeException("ProductionPlan not found with id: " + idPlanning));
        return mapToResponseDTO(productionPlan);
    }

    @Override
    public List<ProductionPlanResponseDTO> getAllProductionPlans() {
        List<ProductionPlan> productionPlans = productionPlanRepository.findAll();
        return productionPlans.stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    private ProductionPlan mapToEntity(ProductionPlanRequestDTO productionPlanRequestDTO, Product product, Workshop workshop) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss.SSS");  // Custom pattern for milliseconds
        LocalTime time = LocalTime.parse(productionPlanRequestDTO.getDuration(), formatter);  // Convert to LocalTime
        return ProductionPlan.builder()
                .date(getDate(productionPlanRequestDTO.getDate()))
                .quantity(productionPlanRequestDTO.getQuantity())
                .duration(time)
                .product(product)
                .workshop(workshop)
                .workforce(productionPlanRequestDTO.getWorkforce())
                .comment(productionPlanRequestDTO.getComment())
                .build();
    }

    private ProductionPlanResponseDTO mapToResponseDTO(ProductionPlan productionPlan) {
        String status = "EN_COURS";
        if(productionPlan.getWaste()!=null && productionPlan.getWaste()>0){
            status = "TERMINE" ;
        } else if (LocalDateTime.now().compareTo(productionPlan.getDate().plusHours(productionPlan.getDuration().getHour()))>0) {
            status = "TERMINE" ;
        }
        FileStorage logo =null ;
        String logoBase64 =null;

        if (productionPlan.getProduct().getLogo() != null) {
            logo  = productionPlan.getProduct().getLogo() ;
            logoBase64 = Base64.getEncoder().encodeToString(logo.getData());
        }
        return ProductionPlanResponseDTO.builder()
                .idPlanning(productionPlan.getIdPlanning())
                .date(productionPlan.getDate().toString())
                .quantity(productionPlan.getQuantity())
                .waste(productionPlan.getWaste())
                .duration(productionPlan.getDuration().toString())
                .productId(productionPlan.getProduct().getIdProduct())
                .workshopId(productionPlan.getWorkshop().getIdWorkshop())
                .workforce(productionPlan.getWorkforce())
                .comment(productionPlan.getComment())
                .status(status)
                .logo(logoBase64!=null?logoBase64:"")
                .logoName(logo != null ?logo.getFileName() : null)
                .logoType(logo!= null? logo.getFileType(): null)
                .build();
    }

    @Override
    public byte[] generatePdf(Long id) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(byteArrayOutputStream);
        PdfDocument pdfDoc = new PdfDocument(writer);
        Document document = new Document(pdfDoc);

        // Set document font, size, and styling
        PdfFont font = PdfFontFactory.createFont(StandardFonts.HELVETICA);
        document.setFont(font);
        document.setFontSize(12);

        Optional<ProductionPlan> existingPlan = productionPlanRepository.findById(id);
        if (existingPlan.isPresent()) {
            ProductionPlan plan = existingPlan.get();

            // Add title in French
            Paragraph title = new Paragraph("Détails du planning")
                    .setFontSize(18)
                    .setBold()
                    .setTextAlignment(TextAlignment.CENTER)
                    .setMarginBottom(20);
            document.add(title);

            // Client info table with null checks and French labels
            Table clientInfoTable = new Table(2);
            clientInfoTable.setWidth(UnitValue.createPercentValue(100));


            // Add a final note in French
            Paragraph finalNote = new Paragraph("Ce document contient les informations détaillées du planning : " + plan.getIdPlanning())
                    .setFontSize(10)
                    .setItalic()
                    .setTextAlignment(TextAlignment.CENTER)
                    .setMarginTop(20);
            document.add(finalNote);
        } else {
            document.add(new Paragraph("Order non trouvé").setFontSize(12).setBold());
        }

        document.close();

        // Convert ByteArrayOutputStream to byte array
        return byteArrayOutputStream.toByteArray();
    }

    private static LocalDateTime getDate(String date){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

        // Parse the date using LocalDateTime and the formatter
        LocalDateTime dateFormatted = LocalDateTime.parse(date.substring(0, 19), formatter);

        return dateFormatted;
    }
}