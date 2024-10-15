package com.example.gestox.dto;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class UserRegistrationDto {

    private String firstName;
    private String lastName;

    private Long phoneNumber;
    private String status;

    private String email;

    private String password;

    private MultipartFile profileImage;

}
