package com.example.gestox.service.RawMaterialService;

import com.example.gestox.dao.RawMaterialRepository;
import com.example.gestox.dto.RawMaterialRequestDTO;
import com.example.gestox.dto.RawMaterialResponseDTO;
import com.example.gestox.entity.ProductionPlan;
import com.example.gestox.entity.RawMaterial;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RawMaterialServiceImpl implements RawMaterialService {

    @Autowired
    private RawMaterialRepository rawMaterialRepository;


    public Page<RawMaterialResponseDTO> getAllRawMaterials(Pageable pageable) {
        Page<RawMaterial> rawMaterials = rawMaterialRepository.findAll(pageable);
        List<RawMaterialResponseDTO> rawMaterialResponseDTOS = rawMaterials.stream().map(rawMaterial -> {
            RawMaterialResponseDTO responseDTO = new RawMaterialResponseDTO();
            responseDTO.setIdMaterial(rawMaterial.getIdMaterial());
            responseDTO.setMaterialType(rawMaterial.getMaterialType());
            responseDTO.setName(rawMaterial.getName());
            responseDTO.setOrigin(rawMaterial.getOrigin());
            responseDTO.setColor(rawMaterial.getColor());
            responseDTO.setSupplier(rawMaterial.getSupplier());
            responseDTO.setAvailableQuantity(rawMaterial.getAvailableQuantity());
            responseDTO.setUnitPrice(rawMaterial.getUnitPrice());
            responseDTO.setUnit(rawMaterial.getUnit());

            //

            return responseDTO;
        }).collect(Collectors.toList());

        // Return a new PageImpl<ProductResponse> to preserve pagination info
        return new PageImpl<>(rawMaterialResponseDTOS, pageable, rawMaterials.getTotalElements());
    }

    @Override
    public RawMaterialResponseDTO createRawMaterial(RawMaterialRequestDTO rawMaterialRequestDTO) {
        RawMaterial rawMaterial = mapToEntity(rawMaterialRequestDTO);
        RawMaterial savedRawMaterial = rawMaterialRepository.save(rawMaterial);
        return mapToResponseDTO(savedRawMaterial);
    }

    @Override
    public RawMaterialResponseDTO updateRawMaterial( RawMaterialRequestDTO rawMaterialRequestDTO) {
        Optional<RawMaterial> rawMaterialOptional = rawMaterialRepository.findById(rawMaterialRequestDTO.getIdMaterial());

        if (rawMaterialOptional.isPresent()) {
            RawMaterial rawMaterial = rawMaterialOptional.get();
            rawMaterial.setName(rawMaterialRequestDTO.getName());
            rawMaterial.setMaterialType(rawMaterialRequestDTO.getMaterialType());
            rawMaterial.setSupplier(rawMaterialRequestDTO.getSupplier());
            rawMaterial.setAvailableQuantity(rawMaterialRequestDTO.getAvailableQuantity());
            rawMaterial.setUnit(rawMaterialRequestDTO.getUnit());
            rawMaterial.setColor(rawMaterialRequestDTO.getColor());
            rawMaterial.setOrigin(rawMaterialRequestDTO.getOrigin());
            rawMaterial.setUnitPrice(rawMaterialRequestDTO.getUnitPrice());


            rawMaterialRepository.save(rawMaterial);

            RawMaterialResponseDTO response = new RawMaterialResponseDTO() ;
            response.setName(rawMaterial.getName());
            response.setMaterialType(rawMaterial.getMaterialType());
            response.setSupplier(rawMaterial.getSupplier());
            response.setAvailableQuantity(rawMaterial.getAvailableQuantity());
            response.setUnit(rawMaterial.getUnit());
            response.setColor(rawMaterial.getColor());
            response.setOrigin(rawMaterial.getOrigin());
            response.setUnitPrice(rawMaterial.getUnitPrice());



            return response ;
        } else {
            throw new RuntimeException("Client not found with id " + rawMaterialRequestDTO.getName());
        }
    }
    @Override
    public void deleteRawMaterial(Long idMaterial) {
        if (rawMaterialRepository.existsById(idMaterial)) {
            rawMaterialRepository.deleteById(idMaterial);
        } else {
            throw new RuntimeException("Raw material not found with id: " + idMaterial);
        }
    }

    @Override
    public RawMaterialResponseDTO getRawMaterialById(Long idMaterial) {
        RawMaterial rawMaterial = rawMaterialRepository.findById(idMaterial)
                .orElseThrow(() -> new RuntimeException("Raw material not found with id: " + idMaterial));
        return mapToResponseDTO(rawMaterial);
    }

    @Override
    public List<RawMaterialResponseDTO> getAllRawMaterials() {
        List<RawMaterial> rawMaterials = rawMaterialRepository.findAll();
        return rawMaterials.stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    private RawMaterial mapToEntity(RawMaterialRequestDTO rawMaterialRequestDTO) {
        return RawMaterial.builder()
                .name(rawMaterialRequestDTO.getName())
                .materialType(rawMaterialRequestDTO.getMaterialType())
                .supplier(rawMaterialRequestDTO.getSupplier())
                .availableQuantity(rawMaterialRequestDTO.getAvailableQuantity())
                .unit(rawMaterialRequestDTO.getUnit())
                .color(rawMaterialRequestDTO.getColor())
                .origin(rawMaterialRequestDTO.getOrigin())
                .unitPrice(rawMaterialRequestDTO.getUnitPrice())
                .build();
    }

    private RawMaterialResponseDTO mapToResponseDTO(RawMaterial rawMaterial) {
        return RawMaterialResponseDTO.builder()
                .idMaterial(rawMaterial.getIdMaterial())
                .name(rawMaterial.getName())
                .materialType(rawMaterial.getMaterialType())
                .supplier(rawMaterial.getSupplier())
                .availableQuantity(rawMaterial.getAvailableQuantity())
                .unit(rawMaterial.getUnit())
                .color(rawMaterial.getColor())
                .origin(rawMaterial.getOrigin())
                .unitPrice(rawMaterial.getUnitPrice())
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

        Optional<RawMaterial> existingMaterial = rawMaterialRepository.findById(id);
        if (existingMaterial.isPresent()) {
            RawMaterial material = existingMaterial.get();

            // Add title in French
            Paragraph title = new Paragraph("Détails de la matiére premiere")
                    .setFontSize(18)
                    .setBold()
                    .setTextAlignment(TextAlignment.CENTER)
                    .setMarginBottom(20);
            document.add(title);

            // Client info table with null checks and French labels
            Table clientInfoTable = new Table(2);
            clientInfoTable.setWidth(UnitValue.createPercentValue(100));


            // Add a final note in French
            Paragraph finalNote = new Paragraph("Ce document contient les informations détaillées de la matiére premiere : " + material.getIdMaterial())
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
}