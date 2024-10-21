package com.example.gestox.service.WorkshopService;

import com.example.gestox.dao.WorkshopRepository;
import com.example.gestox.dto.RawMaterialResponseDTO;
import com.example.gestox.dto.WorkshopRequestDTO;
import com.example.gestox.dto.WorkshopResponseDTO;
import com.example.gestox.entity.RawMaterial;
import com.example.gestox.entity.User;
import com.example.gestox.entity.Workshop;
import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;
import org.hibernate.jdbc.Work;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
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
            workshop.setProductionCapacity(workshopRequestDTO.getProductionCapacity());
            workshop.setWorkshopNumber(workshopRequestDTO.getWorkshopNumber());
            workshop.setMachineCost(workshopRequestDTO.getMachineCost());
            workshop.setMachineCount(workshopRequestDTO.getMachineCount());



            workshopRepository.save(workshop);

            WorkshopResponseDTO response = new WorkshopResponseDTO() ;
            response.setProductionCapacity(workshop.getProductionCapacity());
            response.setWorkshopNumber(workshop.getWorkshopNumber());
            response.setMachineCost(workshop.getMachineCost());
            response.setMachineCount(workshop.getMachineCount());




            return response ;
        } else {
                throw new RuntimeException("Client not found with id " + workshopRequestDTO.getWorkshopNumber());
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

    @Override
    public byte[] generatePdf(Long id) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(byteArrayOutputStream);
        PdfDocument pdfDoc = new PdfDocument(writer);
        Document document = new Document(pdfDoc);

        // Set document font, size, and styling
        PdfFont font = PdfFontFactory.createFont(StandardFonts.HELVETICA);
        document.setFont(font);
        document.setFontSize(12);

        Optional<Workshop> existingWrokshop = workshopRepository.findById(id);
        if (existingWrokshop.isPresent()) {
            Workshop workshop = existingWrokshop.get();

            // Add title in French
            Paragraph title = new Paragraph("Détails de l'atelier")
                    .setFontSize(18)
                    .setBold()
                    .setTextAlignment(TextAlignment.CENTER)
                    .setMarginBottom(20);
            document.add(title);

            // Client info table with null checks and French labels
            Table clientInfoTable = new Table(2);
            clientInfoTable.setWidth(UnitValue.createPercentValue(100));


            // Add a final note in French
            Paragraph finalNote = new Paragraph("Ce document contient les informations détaillées de l'atelier : " + workshop.getWorkshopNumber())
                    .setFontSize(10)
                    .setItalic()
                    .setTextAlignment(TextAlignment.CENTER)
                    .setMarginTop(20);
            document.add(finalNote);
        } else {
            document.add(new Paragraph("atelier non trouvé").setFontSize(12).setBold());
        }

        document.close();

        // Convert ByteArrayOutputStream to byte array
        return byteArrayOutputStream.toByteArray();
    }
}