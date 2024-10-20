package com.example.gestox.service.WorkshopService;

import com.example.gestox.dto.ClientResponseDTO;
import com.example.gestox.dto.WorkshopRequestDTO;
import com.example.gestox.dto.WorkshopResponseDTO;
import com.example.gestox.entity.Workshop;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface WorkshopService {
    WorkshopResponseDTO createWorkshop(WorkshopRequestDTO workshopRequestDTO);
    WorkshopResponseDTO updateWorkshop(WorkshopRequestDTO workshopRequestDTO);
    void deleteWorkshop(Long idWorkshop);
    WorkshopResponseDTO getWorkshopById(Long idWorkshop);
    List<WorkshopResponseDTO> getAllWorkshops();
    public Page<WorkshopResponseDTO> getAllWorkshops(Pageable pageable);

}
