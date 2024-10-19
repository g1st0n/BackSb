package com.example.gestox.service.WorkshopService;

import com.example.gestox.dto.WorkshopRequestDTO;
import com.example.gestox.dto.WorkshopResponseDTO;
import com.example.gestox.entity.Workshop;

import java.io.IOException;
import java.util.List;

public interface WorkshopService {
    WorkshopResponseDTO createWorkshop(WorkshopRequestDTO workshopRequestDTO);
    WorkshopResponseDTO updateWorkshop(Long idWorkshop, WorkshopRequestDTO workshopRequestDTO);
    void deleteWorkshop(Long idWorkshop);
    WorkshopResponseDTO getWorkshopById(Long idWorkshop);
    List<WorkshopResponseDTO> getAllWorkshops();

    public byte[] generatePdf(Long id) throws IOException;
}
