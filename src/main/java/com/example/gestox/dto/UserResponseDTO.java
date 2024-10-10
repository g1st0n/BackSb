package com.example.gestox.dto;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponseDTO {

    private Long id;
    private String firstName;
    private String lastName;
    private String address;
    private Long phoneNumber;
    private String role;
    private String status;
    private Date lastAccess;
    private boolean enabled;
    private String profileImageUrl;  // URL of the profile image (from FileStorage)
}

