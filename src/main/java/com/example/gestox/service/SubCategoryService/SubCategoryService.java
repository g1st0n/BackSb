package com.example.gestox.service.SubCategoryService;

import com.example.gestox.dto.ClientResponseDTO;
import com.example.gestox.dto.SubCategoryRequestDTO;
import com.example.gestox.dto.SubCategoryResponseDTO;
import com.example.gestox.entity.SubCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface SubCategoryService {
    SubCategoryResponseDTO createSubCategory(SubCategoryRequestDTO subCategoryRequestDTO);
    SubCategoryResponseDTO updateSubCategory(SubCategoryRequestDTO subCategoryRequestDTO);
    void deleteSubCategory(Long idSubCategory);
    SubCategoryResponseDTO getSubCategoryById(Long idSubCategory);
    List<SubCategoryResponseDTO> getAllSubCategories();
    public Page<SubCategoryResponseDTO> getAllSubCategories(Pageable pageable);

}

