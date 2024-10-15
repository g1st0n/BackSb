package com.example.gestox.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClientRequestDTO {
    private String fullName;
    private String clientType;
    private String email;
    private String address;
    private Long userId;  // Assuming you want to pass the User ID
}