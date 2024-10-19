package com.example.gestox.service.SubCategoryService;

import com.example.gestox.dto.SubCategoryRequestDTO;
import com.example.gestox.dto.SubCategoryResponseDTO;
import com.example.gestox.entity.SubCategory;

import java.io.IOException;
import java.util.List;

public interface SubCategoryService {
    SubCategoryResponseDTO createSubCategory(SubCategoryRequestDTO subCategoryRequestDTO);
    SubCategoryResponseDTO updateSubCategory(Long idSubCategory, SubCategoryRequestDTO subCategoryRequestDTO);
    void deleteSubCategory(Long idSubCategory);
    SubCategoryResponseDTO getSubCategoryById(Long idSubCategory);
    List<SubCategoryResponseDTO> getAllSubCategories();

    public byte[] generatePdf(Long id) throws IOException;
}

