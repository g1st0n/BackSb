package com.example.gestox.dto;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class FileStorageRequest {

    private MultipartFile file;  // The file to upload (image, document, etc.)

    // You can add additional fields if needed, for example:
    private String description;
    private String fileCategory; // e.g., profile image, document, etc.
}
