package com.example.gestox.dto;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

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
    private MultipartFile image;
}

