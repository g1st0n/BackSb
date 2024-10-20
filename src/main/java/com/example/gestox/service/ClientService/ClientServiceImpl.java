package com.example.gestox.service.ClientService;

import com.example.gestox.dao.ClientRepository;
import com.example.gestox.dao.UserRepository;
import com.example.gestox.dto.ClientRequestDTO;
import com.example.gestox.dto.ClientResponseDTO;
import com.example.gestox.dto.ProductResponse;
import com.example.gestox.dto.RawMaterialRequestDTO;
import com.example.gestox.entity.*;
import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ClientServiceImpl implements ClientService {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private UserRepository userRepository;

    // Create or Update Client
    public ClientResponseDTO saveClient(ClientRequestDTO request) {

        Client client = mapToEntity(request);
        Client savedClient = clientRepository.save(client);
        return mapToResponseDTO(savedClient);
    }
    private Client mapToEntity(ClientRequestDTO clientRequestDTO) {

        return Client.builder()
                .fullName(clientRequestDTO.getFullName())
                .clientType(clientRequestDTO.getClientType())
                .email(clientRequestDTO.getEmail())
                .address(clientRequestDTO.getAddress())
                .telephone(clientRequestDTO.getTelephone())
                .build();
    }

    @Override
    public ClientResponseDTO updateClient(ClientRequestDTO clientRequestDTO) {
        Optional<Client> clientOptional = clientRepository.findById(clientRequestDTO.getClientId());

        if (clientOptional.isPresent()) {
            Client client = clientOptional.get();
            client.setFullName(clientRequestDTO.getFullName());
            client.setClientType(clientRequestDTO.getClientType());
            client.setEmail(clientRequestDTO.getEmail());
            client.setAddress(clientRequestDTO.getAddress());
            client.setTelephone(clientRequestDTO.getTelephone());

            // If user ID is provided, update the associated user
            //Optional<User> userOptional = userRepository.findById(clientRequestDTO.getUserId());
            //userOptional.ifPresent(client::setUser);

            clientRepository.save(client);

            ClientResponseDTO response = new ClientResponseDTO() ;
            response.setAddress(client.getAddress());
            response.setClientType(client.getClientType());
            response.setEmail(client.getEmail());
            response.setFullName(client.getFullName());



            return response ;
        } else {
            throw new RuntimeException("Client not found with id " + clientRequestDTO.getUserId());
        }
    }

    @Override
    public void deleteClient(Long idClient) {
        clientRepository.delete(clientRepository.findById(idClient).get());
    }

    @Override
    public ClientResponseDTO getClientById(Long idClient) {
        Optional<Client> clientOptional = clientRepository.findById(idClient);
        Client client = null ;
        if (clientOptional.isPresent()) {
            client = clientOptional.get();
        }
        return mapToResponseDTO(client);
    }

    // Get All Clients
    public List<ClientResponseDTO> getAllClients() {
        List<Client> clients = clientRepository.findAll();
        return clients.stream().map(this::mapToResponseDTO).collect(Collectors.toList());
    }

    // Map Client entity to Response DTO
    private ClientResponseDTO mapToResponseDTO(Client client) {
        ClientResponseDTO responseDTO = new ClientResponseDTO();
        responseDTO.setIdClient(client.getIdClient());
        responseDTO.setFullName(client.getFullName());
        responseDTO.setClientType(client.getClientType());
        responseDTO.setEmail(client.getEmail());
        responseDTO.setAddress(client.getAddress());
        responseDTO.setTelephone(client.getTelephone());

        // Set user inf}

        return responseDTO;
    }

    @Override
    public Page<ClientResponseDTO> getAllClients(Pageable pageable) {
        Page<Client> clients = clientRepository.findAll(pageable);
        List<ClientResponseDTO> productResponseDTOs = clients.stream().map(client -> {
            ClientResponseDTO responseDTO = new ClientResponseDTO();
            responseDTO.setIdClient(client.getIdClient());
            responseDTO.setFullName(client.getFullName());
            responseDTO.setClientType(client.getClientType());
            responseDTO.setEmail(client.getEmail());
            responseDTO.setAddress(client.getAddress());
            responseDTO.setTelephone(client.getTelephone());

            //
            if (client.getUser() != null) {
                responseDTO.setUserId(client.getUser().getId());
                responseDTO.setUserFullName(client.getUser().getFirstName() + " " + client.getUser().getLastName());
            }
            return responseDTO;
        }).collect(Collectors.toList());

        // Return a new PageImpl<ProductResponse> to preserve pagination info
        return new PageImpl<>(productResponseDTOs, pageable, clients.getTotalElements());
    }

    @Override
    public byte[] generatePdf(Long clientId) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(byteArrayOutputStream);
        PdfDocument pdfDoc = new PdfDocument(writer);
        Document document = new Document(pdfDoc);

        // Set document font, size, and styling
        PdfFont font = PdfFontFactory.createFont(StandardFonts.HELVETICA);
        document.setFont(font);
        document.setFontSize(12);

        Optional<Client> existingClient = clientRepository.findById(clientId);
        if (existingClient.isPresent()) {
            Client client = existingClient.get();

            // Add title in French
            Paragraph title = new Paragraph("Détails du Client")
                    .setFontSize(18)
                    .setBold()
                    .setTextAlignment(TextAlignment.CENTER)
                    .setMarginBottom(20);
            document.add(title);

            // Client info table with null checks and French labels
            Table clientInfoTable = new Table(2);
            clientInfoTable.setWidth(UnitValue.createPercentValue(100));

            clientInfoTable.addCell(new Cell().add(new Paragraph("Nom et prénom :")));
            clientInfoTable.addCell(new Cell().add(new Paragraph(client.getFullName() != null ? client.getFullName() : "Non renseigné")));

            clientInfoTable.addCell(new Cell().add(new Paragraph("Type du client :")));
            clientInfoTable.addCell(new Cell().add(new Paragraph(client.getClientType() != null ? client.getClientType() : "Non renseigné")));

            clientInfoTable.addCell(new Cell().add(new Paragraph("Adresse du client :")));
            clientInfoTable.addCell(new Cell().add(new Paragraph(client.getAddress() != null ? client.getAddress() : "Non renseigné")));

            clientInfoTable.addCell(new Cell().add(new Paragraph("Email du client :")));
            clientInfoTable.addCell(new Cell().add(new Paragraph(client.getEmail() != null ? client.getEmail() : "Non renseigné")));

            clientInfoTable.addCell(new Cell().add(new Paragraph("Numéro de téléphone :")));
            clientInfoTable.addCell(new Cell().add(new Paragraph(client.getTelephone() != null ? client.getTelephone() : "Non renseigné")));

            document.add(clientInfoTable);

            // Retrieve client orders (assuming you have a method to get orders)
            List<CustomerOrder> clientOrders = client.getOrders();
            if (clientOrders != null && !clientOrders.isEmpty()) {
                // Add a subtitle for the order table
                Paragraph orderTitle = new Paragraph("Commandes du Client")
                        .setFontSize(16)
                        .setBold()
                        .setTextAlignment(TextAlignment.CENTER)
                        .setMarginTop(20)
                        .setMarginBottom(10);
                document.add(orderTitle);

                // Orders table with adjusted column widths
                Table ordersTable = new Table(new float[]{3, 2, 2}); // Define column widths properly
                ordersTable.setWidth(UnitValue.createPercentValue(100));

                // Add table headers with proper alignment and bold text
                ordersTable.addCell(new Cell().add(new Paragraph("ID de Commande").setBold()).setTextAlignment(TextAlignment.CENTER));
                ordersTable.addCell(new Cell().add(new Paragraph("Produit").setBold()).setTextAlignment(TextAlignment.CENTER));
                ordersTable.addCell(new Cell().add(new Paragraph("Quantité").setBold()).setTextAlignment(TextAlignment.CENTER));

                // Add order data
                for (CustomerOrder order : clientOrders) {
                    ordersTable.addCell(new Cell().add(new Paragraph(order.getIdOrder().toString())).setTextAlignment(TextAlignment.CENTER));
                    ordersTable.addCell(new Cell().add(new Paragraph(order.getProduct().getReference())).setTextAlignment(TextAlignment.CENTER));
                    ordersTable.addCell(new Cell().add(new Paragraph(order.getQuantity().toString())).setTextAlignment(TextAlignment.CENTER));
                }

                // Add the orders table to the document
                document.add(ordersTable);
            } else {
                document.add(new Paragraph("Aucune commande trouvée pour ce client.").setItalic());
            }

            // Add a final note in French
            Paragraph finalNote = new Paragraph("Ce document contient les informations détaillées du client : " + client.getFullName())
                    .setFontSize(10)
                    .setItalic()
                    .setTextAlignment(TextAlignment.CENTER)
                    .setMarginTop(20);
            document.add(finalNote);
        } else {
            document.add(new Paragraph("Client non trouvé").setFontSize(12).setBold());
        }

        document.close();

        // Convert ByteArrayOutputStream to byte array
        return byteArrayOutputStream.toByteArray();
    }

}

