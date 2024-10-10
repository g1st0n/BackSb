package com.example.gestox.service.ClientService;

import com.example.gestox.dto.ClientRequestDTO;
import com.example.gestox.dto.ClientResponseDTO;
import com.example.gestox.entity.Client;

import java.util.List;

public interface ClientService {
    ClientResponseDTO saveClient(ClientRequestDTO client);
    ClientResponseDTO updateClient(Long idClient, ClientRequestDTO client);
    void deleteClient(Long idClient);
    Client getClientById(Long idClient);
    List<ClientResponseDTO> getAllClients();
}

