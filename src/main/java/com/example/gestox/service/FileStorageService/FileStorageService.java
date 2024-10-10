package com.example.gestox.service.FileStorageService;

import com.example.gestox.entity.FileStorage;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface FileStorageService {

    public FileStorage saveFile(MultipartFile file) throws IOException;
    public FileStorage getFile(Long id) ;

    public List<FileStorage> getAllFiles();

    public void deleteFile(Long id);
}
