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

    @Override
    public RawMaterialResponseDTO createRawMaterial(RawMaterialRequestDTO rawMaterialRequestDTO) {
        RawMaterial rawMaterial = mapToEntity(rawMaterialRequestDTO);
        RawMaterial savedRawMaterial = rawMaterialRepository.save(rawMaterial);
        return mapToResponseDTO(savedRawMaterial);
    }

    @Override
    public RawMaterialResponseDTO updateRawMaterial(Long idMaterial, RawMaterialRequestDTO rawMaterialRequestDTO) {
        RawMaterial rawMaterial = rawMaterialRepository.findById(idMaterial)
                .orElseThrow(() -> new RuntimeException("Raw material not found with id: " + idMaterial));

        // Update raw material details
        rawMaterial.setName(rawMaterialRequestDTO.getName());
        rawMaterial.setMaterialType(rawMaterialRequestDTO.getMaterialType());
        rawMaterial.setSupplier(rawMaterialRequestDTO.getSupplier());
        rawMaterial.setAvailableQuantity(rawMaterialRequestDTO.getAvailableQuantity());
        rawMaterial.setUnit(rawMaterialRequestDTO.getUnit());
        rawMaterial.setColor(rawMaterialRequestDTO.getColor());
        rawMaterial.setOrigin(rawMaterialRequestDTO.getOrigin());
        rawMaterial.setUnitPrice(rawMaterialRequestDTO.getUnitPrice());

        RawMaterial updatedRawMaterial = rawMaterialRepository.save(rawMaterial);
        return mapToResponseDTO(updatedRawMaterial);
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