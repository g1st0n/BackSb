package com.example.gestox.service.ClientService;

import com.example.gestox.dto.ClientRequestDTO;
import com.example.gestox.dto.ClientResponseDTO;
import com.example.gestox.dto.ProductResponse;
import com.example.gestox.entity.Client;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ClientService {
    ClientResponseDTO saveClient(ClientRequestDTO client);
    ClientResponseDTO updateClient(ClientRequestDTO client);
    void deleteClient(Long idClient);
    ClientResponseDTO getClientById(Long idClient);
    List<ClientResponseDTO> getAllClients();
    public Page<ClientResponseDTO> getAllClients(Pageable pageable);
}

