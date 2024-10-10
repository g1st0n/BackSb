package com.example.gestox.dao;

import com.example.gestox.entity.FileStorage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileStorageRepository extends JpaRepository<FileStorage, Long> {

    Boolean existsByFileNameAndFileType(String fileName, String fileType);

    FileStorage findByFileNameAndFileType(String fileName, String fileType);


}
