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

    private String username;

    private String email;

    private String password;

    private MultipartFile profileImage;

}
