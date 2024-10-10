package com.example.gestox.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponse {

    private Long id;

    private String firstName;

    private String lastName;

    private String address;

    private Long phoneNumber;

    private String role;

    private String status;

    private Date lastAccess;

    private boolean enabled;

    // File information for profile image
    private String profileImageUrl;
    private String profileImageFileName;
    private String profileImageFileType;
}
