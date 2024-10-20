package com.example.gestox.controller;

import com.example.gestox.dto.ClientRequestDTO;
import com.example.gestox.dto.ClientResponseDTO;
import com.example.gestox.dto.ProductResponse;
import com.example.gestox.service.ClientService.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clients")
public class ClientController {

    @Autowired
    private ClientService clientService;

    @GetMapping
    public ResponseEntity<Page<ClientResponseDTO>> getAllClients(Pageable pageable) {
        Page<ClientResponseDTO> clients = clientService.getAllClients(pageable);
        return ResponseEntity.ok(clients);
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
        return ResponseEntity.ok(clients);}

    @PutMapping("/{id}")
    public ResponseEntity<ClientResponseDTO> updateClient(@RequestBody ClientRequestDTO clientRequestDTO) {
        ClientResponseDTO clientResponse = clientService.updateClient(clientRequestDTO);
        return ResponseEntity.ok(clientResponse);
    }

    @DeleteMapping("/{id}")
    public void deleteClient(@PathVariable Long id) {
        clientService.deleteClient(id);
    }
}

