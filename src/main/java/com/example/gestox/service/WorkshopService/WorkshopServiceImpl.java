package com.example.gestox.service.WorkshopService;

import com.example.gestox.dao.WorkshopRepository;
import com.example.gestox.dto.ClientResponseDTO;
import com.example.gestox.dto.WorkshopRequestDTO;
import com.example.gestox.dto.WorkshopResponseDTO;
import com.example.gestox.entity.Client;
import com.example.gestox.entity.Workshop;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
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
    public WorkshopResponseDTO updateWorkshop( WorkshopRequestDTO workshopRequestDTO) {
        Optional<Workshop> workshopOptional = workshopRepository.findById(workshopRequestDTO.getIdWorkshop());

        if (workshopOptional.isPresent()) {
            Workshop workshop = workshopOptional.get();
            workshop.setWorkshopNumber(workshopRequestDTO.getWorkshopNumber());
            workshop.setMachineCost(workshopRequestDTO.getMachineCost());
            workshop.setMachineCount(workshopRequestDTO.getMachineCount());
            workshop.setProductionCapacity(workshopRequestDTO.getProductionCapacity());

            // If user ID is provided, update the associated user
            //Optional<User> userOptional = userRepository.findById(clientRequestDTO.getUserId());
            //userOptional.ifPresent(client::setUser);

            workshopRepository.save(workshop);

            WorkshopResponseDTO response = new WorkshopResponseDTO() ;

            response.setProductionCapacity(workshop.getProductionCapacity());
            response.setWorkshopNumber(workshop.getWorkshopNumber());
            response.setMachineCount(workshop.getMachineCount());
            response.setMachineCost(workshop.getMachineCost());



            return response ;
        } else {
            throw new RuntimeException("Workshop not found with id " + workshopRequestDTO.getWorkshopNumber());
        }
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

    @Override
    public Page<WorkshopResponseDTO> getAllWorkshops(Pageable pageable) {
        Page<Workshop> workshops = workshopRepository.findAll(pageable);
        List<WorkshopResponseDTO> workshopResponseDTOS = workshops.stream().map(workshop -> {
            WorkshopResponseDTO responseDTO = new WorkshopResponseDTO();
            responseDTO.setIdWorkshop(workshop.getIdWorkshop());
            responseDTO.setWorkshopNumber(workshop.getWorkshopNumber());
            responseDTO.setMachineCost(workshop.getMachineCost());
            responseDTO.setMachineCount(workshop.getMachineCount());
            responseDTO.setProductionCapacity(workshop.getProductionCapacity());

            //

            return responseDTO;
        }).collect(Collectors.toList());

        // Return a new PageImpl<ProductResponse> to preserve pagination info
        return new PageImpl<>(workshopResponseDTOS, pageable, workshops.getTotalElements());
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