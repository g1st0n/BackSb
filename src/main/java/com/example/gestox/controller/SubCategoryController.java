package com.example.gestox.controller;

import com.example.gestox.dto.SubCategoryRequestDTO;
import com.example.gestox.dto.SubCategoryResponseDTO;
import com.example.gestox.service.SubCategoryService.SubCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/subcategories")
public class SubCategoryController {

    @Autowired
    private SubCategoryService subCategoryService;

    // Create a new SubCategory
    @PostMapping("/add")
    public ResponseEntity<SubCategoryResponseDTO> createSubCategory(@RequestBody SubCategoryRequestDTO subCategoryRequestDTO) {
        SubCategoryResponseDTO createdSubCategory = subCategoryService.createSubCategory(subCategoryRequestDTO);
        return ResponseEntity.ok(createdSubCategory);
    }

    // Update an existing SubCategory by ID
    @PutMapping("/{id}")
    public ResponseEntity<SubCategoryResponseDTO> updateSubCategory(
            @PathVariable Long id,
            @RequestBody SubCategoryRequestDTO subCategoryRequestDTO) {
        SubCategoryResponseDTO updatedSubCategory = subCategoryService.updateSubCategory(id, subCategoryRequestDTO);
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
    @GetMapping
    public ResponseEntity<List<SubCategoryResponseDTO>> getAllSubCategories() {
        List<SubCategoryResponseDTO> subCategories = subCategoryService.getAllSubCategories();
        return ResponseEntity.ok(subCategories);
    }
}
