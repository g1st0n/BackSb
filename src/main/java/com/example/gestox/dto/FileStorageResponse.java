package com.example.gestox.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FileStorageResponse {

    private Long id;
    private String fileName;
    private String fileType;
    private LocalDateTime creationDate;
}

