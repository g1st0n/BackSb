package com.example.gestox.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClientRequestDTO {
    private Long clientId;
    private String fullName;
    private String clientType;
    private String email;
    private String address;
    private String telephone ;
    private Long userId;
}
