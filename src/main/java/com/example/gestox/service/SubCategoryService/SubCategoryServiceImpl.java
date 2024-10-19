package com.example.gestox.service.SubCategoryService;

import com.example.gestox.dao.SubCategoryRepository;
import com.example.gestox.dto.SubCategoryRequestDTO;
import com.example.gestox.dto.SubCategoryResponseDTO;
import com.example.gestox.entity.RawMaterial;
import com.example.gestox.entity.SubCategory;
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
public class SubCategoryServiceImpl implements SubCategoryService {

    @Autowired
    private SubCategoryRepository subCategoryRepository;

    @Override
    public SubCategoryResponseDTO createSubCategory(SubCategoryRequestDTO subCategoryRequestDTO) {
        SubCategory subCategory = mapToEntity(subCategoryRequestDTO);
        SubCategory savedSubCategory = subCategoryRepository.save(subCategory);
        return mapToResponseDTO(savedSubCategory);
    }

    @Override
    public SubCategoryResponseDTO updateSubCategory(Long idSubCategory, SubCategoryRequestDTO subCategoryRequestDTO) {
        SubCategory subCategory = subCategoryRepository.findById(idSubCategory)
                .orElseThrow(() -> new RuntimeException("SubCategory not found with id: " + idSubCategory));

        // Update subcategory details
        subCategory.setName(subCategoryRequestDTO.getName());
        subCategory.setReference(subCategoryRequestDTO.getReference());

        SubCategory updatedSubCategory = subCategoryRepository.save(subCategory);
        return mapToResponseDTO(updatedSubCategory);
    }

    @Override
    public void deleteSubCategory(Long idSubCategory) {
        if (subCategoryRepository.existsById(idSubCategory)) {
            subCategoryRepository.deleteById(idSubCategory);
        } else {
            throw new RuntimeException("SubCategory not found with id: " + idSubCategory);
        }
    }

    @Override
    public SubCategoryResponseDTO getSubCategoryById(Long idSubCategory) {
        SubCategory subCategory = subCategoryRepository.findById(idSubCategory)
                .orElseThrow(() -> new RuntimeException("SubCategory not found with id: " + idSubCategory));
        return mapToResponseDTO(subCategory);
    }

    @Override
    public List<SubCategoryResponseDTO> getAllSubCategories() {
        List<SubCategory> subCategories = subCategoryRepository.findAll();
        return subCategories.stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    private SubCategory mapToEntity(SubCategoryRequestDTO subCategoryRequestDTO) {
        return SubCategory.builder()
                .name(subCategoryRequestDTO.getName())
                .reference(subCategoryRequestDTO.getReference())
                .build();
    }

    private SubCategoryResponseDTO mapToResponseDTO(SubCategory subCategory) {
        return SubCategoryResponseDTO.builder()
                .idSubCategory(subCategory.getIdSubCategory())
                .name(subCategory.getName())
                .reference(subCategory.getReference())
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

        Optional<SubCategory> existingSubCat = subCategoryRepository.findById(id);
        if (existingSubCat.isPresent()) {
            SubCategory subCat = existingSubCat.get();

            // Add title in French
            Paragraph title = new Paragraph("Détails de la sous-categorie")
                    .setFontSize(18)
                    .setBold()
                    .setTextAlignment(TextAlignment.CENTER)
                    .setMarginBottom(20);
            document.add(title);

            // Client info table with null checks and French labels
            Table clientInfoTable = new Table(2);
            clientInfoTable.setWidth(UnitValue.createPercentValue(100));


            // Add a final note in French
            Paragraph finalNote = new Paragraph("Ce document contient les informations détaillées de la sous-categorie : " + subCat.getReference())
                    .setFontSize(10)
                    .setItalic()
                    .setTextAlignment(TextAlignment.CENTER)
                    .setMarginTop(20);
            document.add(finalNote);
        } else {
            document.add(new Paragraph("sous-categorie non trouvé").setFontSize(12).setBold());
        }

        document.close();

        // Convert ByteArrayOutputStream to byte array
        return byteArrayOutputStream.toByteArray();
    }
}