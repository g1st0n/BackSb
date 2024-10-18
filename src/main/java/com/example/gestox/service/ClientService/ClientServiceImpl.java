package com.example.gestox.service.ClientService;

import com.example.gestox.dao.ClientRepository;
import com.example.gestox.dao.UserRepository;
import com.example.gestox.dto.ClientRequestDTO;
import com.example.gestox.dto.ClientResponseDTO;
import com.example.gestox.dto.RawMaterialRequestDTO;
import com.example.gestox.entity.Client;
import com.example.gestox.entity.RawMaterial;
import com.example.gestox.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public ClientResponseDTO updateClient(Long idClient, ClientRequestDTO clientRequestDTO) {
        Optional<Client> clientOptional = clientRepository.findById(idClient);

        if (clientOptional.isPresent()) {
            Client client = clientOptional.get();
            client.setFullName(clientRequestDTO.getFullName());
            client.setClientType(clientRequestDTO.getClientType());
            client.setEmail(clientRequestDTO.getEmail());
            client.setAddress(clientRequestDTO.getAddress());
            client.setTelephone(clientRequestDTO.getTelephone());



            // If user ID is provided, update the associated user
            Optional<User> userOptional = userRepository.findById(clientRequestDTO.getUserId());
            userOptional.ifPresent(client::setUser);

            clientRepository.save(client);  // Save the updated client
            ClientResponseDTO response = new ClientResponseDTO() ;
            response.setAddress(client.getAddress());
            response.setClientType(client.getClientType());
            response.setEmail(client.getEmail());
            response.setFullName(client.getFullName());
            client.setTelephone(client.getTelephone());

            return response ;
        } else {
            throw new RuntimeException("Client not found with id " + idClient);
        }
    }

    @Override
    public void deleteClient(Long idClient) {

    }

    @Override
    public Client getClientById(Long idClient) {
        return null;
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

        // Set user info
        if (client.getUser() != null) {
            responseDTO.setUserId(client.getUser().getId());
            responseDTO.setUserFullName(client.getUser().getFirstName() + " " + client.getUser().getLastName());
        }

        return responseDTO;
    }
}

