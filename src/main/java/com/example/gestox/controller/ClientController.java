package com.example.gestox.controller;

import com.example.gestox.dto.ClientRequestDTO;
import com.example.gestox.dto.ClientResponseDTO;
import com.example.gestox.dto.ProductResponse;
import com.example.gestox.service.ClientService.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clients")
public class ClientController {

    @Autowired
    private ClientService clientService;

    @GetMapping
    public Page<ClientResponseDTO> getClients(Pageable pageable) {
        return clientService.getAllClients(pageable);
    }

    // Create or update client
    @PostMapping("/add")
    public ResponseEntity<ClientResponseDTO> createClient(@RequestBody ClientRequestDTO clientRequestDTO) {
        ClientResponseDTO clientResponse = clientService.saveClient(clientRequestDTO);
        return ResponseEntity.ok(clientResponse);
    }

    // Get all clients
    @GetMapping("/showAll")

    public ResponseEntity<List<ClientResponseDTO>> getAllClients() {
        List<ClientResponseDTO> clients = clientService.getAllClients();
        return ResponseEntity.ok(clients);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClientResponseDTO> updateClient(@RequestBody ClientRequestDTO clientRequestDTO) {
        ClientResponseDTO clientResponse = clientService.updateClient(clientRequestDTO);
        return ResponseEntity.ok(clientResponse);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('FINANCIER')")
    public void deleteClient(@PathVariable Long id) {
        clientService.deleteClient(id);
    }

    @GetMapping("/generate/{clientId}")
    @PreAuthorize("hasRole('FINANCIER')")
    public ResponseEntity<byte[]> generatePdf(@PathVariable Long clientId) {
        try {
            byte[] pdfBytes  =clientService.generatePdf(clientId);

            // Return the generated PDF as a response
            HttpHeaders headers = new HttpHeaders();
            headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=client_" + clientId + ".pdf");
            headers.setContentType(MediaType.APPLICATION_PDF);

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(pdfBytes);

        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }
}

