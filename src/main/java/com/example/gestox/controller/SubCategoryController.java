package com.example.gestox.controller;

import com.example.gestox.dto.ClientResponseDTO;
import com.example.gestox.dto.SubCategoryRequestDTO;
import com.example.gestox.dto.SubCategoryResponseDTO;
import com.example.gestox.service.SubCategoryService.SubCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/subcategories")
public class SubCategoryController {

    @Autowired
    private SubCategoryService subCategoryService;

    @GetMapping
    public Page<SubCategoryResponseDTO> getSubCategories(Pageable pageable) {
        return subCategoryService.getAllSubCategories(pageable);
    }
    // Create a new SubCategory
    @PostMapping("/add")
    public ResponseEntity<SubCategoryResponseDTO> createSubCategory(@RequestBody SubCategoryRequestDTO subCategoryRequestDTO) {
        SubCategoryResponseDTO createdSubCategory = subCategoryService.createSubCategory(subCategoryRequestDTO);
        return ResponseEntity.ok(createdSubCategory);
    }

    // Update an existing SubCategory by ID
    @PutMapping("/{id}")
    public ResponseEntity<SubCategoryResponseDTO> updateSubCategory(@RequestBody SubCategoryRequestDTO subCategoryRequestDTO) {
        SubCategoryResponseDTO updatedSubCategory = subCategoryService.updateSubCategory(subCategoryRequestDTO);
        return ResponseEntity.ok(updatedSubCategory);
    }


    // Delete a SubCategory by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSubCategory(@PathVariable Long id) {
        subCategoryService.deleteSubCategory(id);
        return ResponseEntity.noContent().build(); // Return 204 No Content on successful deletion
    }

    // Get a SubCategory by ID
    @GetMapping("/{id}")
    public ResponseEntity<SubCategoryResponseDTO> getSubCategoryById(@PathVariable Long id) {
        SubCategoryResponseDTO subCategoryResponseDTO = subCategoryService.getSubCategoryById(id);
        return ResponseEntity.ok(subCategoryResponseDTO);
    }

    // Get all SubCategories
    @GetMapping(path = "/showAll")
    public ResponseEntity<List<SubCategoryResponseDTO>> getAllSubCategories() {
        List<SubCategoryResponseDTO> subCategories = subCategoryService.getAllSubCategories();
        return ResponseEntity.ok(subCategories);
    }
}
