package com.example.gestox.service.ClientService;

import com.example.gestox.dao.ClientRepository;
import com.example.gestox.dao.UserRepository;
import com.example.gestox.dto.ClientRequestDTO;
import com.example.gestox.dto.ClientResponseDTO;
import com.example.gestox.dto.ProductResponse;
import com.example.gestox.dto.RawMaterialRequestDTO;
import com.example.gestox.entity.Client;
import com.example.gestox.entity.Product;
import com.example.gestox.entity.RawMaterial;
import com.example.gestox.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

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
}

