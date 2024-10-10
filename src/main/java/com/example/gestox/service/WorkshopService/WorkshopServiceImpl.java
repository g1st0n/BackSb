package com.example.gestox.service.WorkshopService;

import com.example.gestox.dao.WorkshopRepository;
import com.example.gestox.dto.WorkshopRequestDTO;
import com.example.gestox.dto.WorkshopResponseDTO;
import com.example.gestox.entity.Workshop;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class WorkshopServiceImpl implements WorkshopService {

    @Autowired
    private WorkshopRepository workshopRepository;

    @Override
    public WorkshopResponseDTO createWorkshop(WorkshopRequestDTO workshopRequestDTO) {
        Workshop workshop = mapToEntity(workshopRequestDTO);
        Workshop savedWorkshop = workshopRepository.save(workshop);
        return mapToResponseDTO(savedWorkshop);
    }

    @Override
    public WorkshopResponseDTO updateWorkshop(Long idWorkshop, WorkshopRequestDTO workshopRequestDTO) {
        Workshop workshop = workshopRepository.findById(idWorkshop)
                .orElseThrow(() -> new RuntimeException("Workshop not found with id: " + idWorkshop));

        // Update workshop details
        workshop.setWorkshopNumber(workshopRequestDTO.getWorkshopNumber());
        workshop.setProductionCapacity(workshopRequestDTO.getProductionCapacity());
        workshop.setMachineCount(workshopRequestDTO.getMachineCount());
        workshop.setMachineCost(workshopRequestDTO.getMachineCost());

        Workshop updatedWorkshop = workshopRepository.save(workshop);
        return mapToResponseDTO(updatedWorkshop);
    }

    @Override
    public void deleteWorkshop(Long idWorkshop) {
        if (workshopRepository.existsById(idWorkshop)) {
            workshopRepository.deleteById(idWorkshop);
        } else {
            throw new RuntimeException("Workshop not found with id: " + idWorkshop);
        }
    }

    @Override
    public WorkshopResponseDTO getWorkshopById(Long idWorkshop) {
        Workshop workshop = workshopRepository.findById(idWorkshop)
                .orElseThrow(() -> new RuntimeException("Workshop not found with id: " + idWorkshop));
        return mapToResponseDTO(workshop);
    }

    @Override
    public List<WorkshopResponseDTO> getAllWorkshops() {
        List<Workshop> workshops = workshopRepository.findAll();
        return workshops.stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    private Workshop mapToEntity(WorkshopRequestDTO workshopRequestDTO) {
        return Workshop.builder()
                .workshopNumber(workshopRequestDTO.getWorkshopNumber())
                .productionCapacity(workshopRequestDTO.getProductionCapacity())
                .machineCount(workshopRequestDTO.getMachineCount())
                .machineCost(workshopRequestDTO.getMachineCost())
                .build();
    }

    private WorkshopResponseDTO mapToResponseDTO(Workshop workshop) {
        return WorkshopResponseDTO.builder()
                .idWorkshop(workshop.getIdWorkshop())
                .workshopNumber(workshop.getWorkshopNumber())
                .productionCapacity(workshop.getProductionCapacity())
                .machineCount(workshop.getMachineCount())
                .machineCost(workshop.getMachineCost())
                .build();
    }
}