package com.example.gestox.service.SubCategoryService;

import com.example.gestox.dao.SubCategoryRepository;
import com.example.gestox.dto.SubCategoryRequestDTO;
import com.example.gestox.dto.SubCategoryResponseDTO;
import com.example.gestox.entity.SubCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}