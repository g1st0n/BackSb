package com.example.gestox.service.SubCategoryService;

import com.example.gestox.dao.SubCategoryRepository;
import com.example.gestox.dto.ClientResponseDTO;
import com.example.gestox.dto.SubCategoryRequestDTO;
import com.example.gestox.dto.SubCategoryResponseDTO;
import com.example.gestox.entity.Client;
import com.example.gestox.entity.SubCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
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
    public SubCategoryResponseDTO updateSubCategory(SubCategoryRequestDTO subCategoryRequestDTO) {
        Optional<SubCategory> subCategoryOptional = subCategoryRepository.findById(subCategoryRequestDTO.getIdSubCategory());

        if (subCategoryOptional.isPresent()) {
            SubCategory subCategory = subCategoryOptional.get();
            subCategory.setName(subCategoryRequestDTO.getName());
            subCategory.setReference(subCategoryRequestDTO.getReference());


            // If user ID is provided, update the associated user
            //Optional<User> userOptional = userRepository.findById(clientRequestDTO.getUserId());
            //userOptional.ifPresent(client::setUser);

            subCategoryRepository.save(subCategory);

            SubCategoryResponseDTO response = new SubCategoryResponseDTO();
            response.setReference(subCategory.getReference());
            response.setName(subCategory.getName());


            return response;
        } else {
            throw new RuntimeException("Client not found with id " + subCategoryRequestDTO.getName());
        }
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

    @Override
    public Page<SubCategoryResponseDTO> getAllSubCategories(Pageable pageable) {
        Page<SubCategory> subCategories = subCategoryRepository.findAll(pageable);
        List<SubCategoryResponseDTO> subCategoryResponseDTOS = subCategories.stream().map(subCategory -> {
            SubCategoryResponseDTO responseDTO = new SubCategoryResponseDTO();
            responseDTO.setIdSubCategory(subCategory.getIdSubCategory());
            responseDTO.setName(subCategory.getName());
            responseDTO.setReference(subCategory.getReference());


            return responseDTO;
        }).collect(Collectors.toList());

        // Return a new PageImpl<ProductResponse> to preserve pagination info
        return new PageImpl<>(subCategoryResponseDTOS, pageable, subCategories.getTotalElements());
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