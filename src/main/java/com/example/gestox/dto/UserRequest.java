package com.example.gestox.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRequest {

    private String firstName;

    private String lastName;

    private String address;

    private Long phoneNumber;

    private String password;

    private String role;

    private boolean enabled;

    // Profile image (if being uploaded via request)
    private Long profileImageId;
}
