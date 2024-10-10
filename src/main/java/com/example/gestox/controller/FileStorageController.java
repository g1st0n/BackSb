package com.example.gestox.controller;

import com.example.gestox.dto.FileStorageRequest;
import com.example.gestox.dto.FileStorageResponse;
import com.example.gestox.entity.FileStorage;
import com.example.gestox.service.FileStorageService.FileStorageService;
import com.example.gestox.utility.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/files")
public class FileStorageController {

    @Autowired
    private FileStorageService fileStorageService;

    // Endpoint to upload a file
    @PostMapping("/upload")
    public ResponseEntity<FileStorageResponse> uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
        // Logic for saving the file (e.g., store it in the database or on disk)
        FileStorage savedFile = fileStorageService.saveFile(file);

        // Create response containing file metadata (like ID, file name, etc.)
        FileStorageResponse response = new FileStorageResponse(savedFile.getId(), savedFile.getFileName(), savedFile.getFileType(), LocalDateTime.now());
        return ResponseEntity.ok(response);
    }

    // Endpoint to retrieve a file by its ID
    @GetMapping("/{id}")
    public ResponseEntity<byte[]> getFile(@PathVariable Long id) {
        FileStorage fileStorage = fileStorageService.getFile(id);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(fileStorage.getFileType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileStorage.getFileName() + "\"")
                .body(fileStorage.getData());
    }

    // Endpoint to list all files (optional)
    @GetMapping("/list")
    public ResponseEntity<List<FileStorageResponse>> listAllFiles() {
        List<FileStorage> files = fileStorageService.getAllFiles();
        List<FileStorageResponse> response = files.stream()
                .map(Mapper::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    // Endpoint to delete a file by ID (optional)
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteFile(@PathVariable Long id) {
        fileStorageService.deleteFile(id);
        return ResponseEntity.ok("File deleted successfully");
    }

}

