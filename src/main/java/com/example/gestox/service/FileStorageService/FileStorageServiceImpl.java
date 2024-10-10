package com.example.gestox.service.FileStorageService;

import com.example.gestox.dao.FileStorageRepository;
import com.example.gestox.entity.FileStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class FileStorageServiceImpl implements FileStorageService {

    @Autowired
    private FileStorageRepository fileStorageRepository;

    @Override
    public FileStorage saveFile(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new IOException("File is empty");
        }

        FileStorage fileStorage = new FileStorage();
        fileStorage.setFileName(file.getOriginalFilename());
        fileStorage.setFileType(file.getContentType());
        fileStorage.setData(file.getBytes());
        fileStorage.setCreationDate(LocalDateTime.now());

        return fileStorageRepository.save(fileStorage);
    }

    @Override
    public FileStorage getFile(Long id) {
        return fileStorageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("File not found with id " + id));
    }

    @Override
    public List<FileStorage> getAllFiles() {
        return fileStorageRepository.findAll();
    }

    @Override
    public void deleteFile(Long id) {
        fileStorageRepository.deleteById(id);
    }
}

