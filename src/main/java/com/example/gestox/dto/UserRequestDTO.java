package com.example.gestox.dto;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRequestDTO {

    private String firstName;
    private String lastName;
    private String address;
    private Long phoneNumber;
    private String password;
    private String role;
    private String status;
    private Date lastAccess;
    private boolean enabled;
    private Long profileImageId; // Reference to FileStorage entity for the profile image
}

