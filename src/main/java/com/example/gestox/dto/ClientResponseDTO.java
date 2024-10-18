package com.example.gestox.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClientResponseDTO {
    private Long idClient;
    private String fullName;
    private String clientType;
    private String email;
    private String address;
    private String telephone ;

    private Long userId;
    private String userFullName;  // Assuming you want to return some info about the user
}
