package com.example.gestox.controller;

import com.example.gestox.dto.OrderResponseDTO;
import com.example.gestox.dto.UserRequestDTO;
import com.example.gestox.dto.UserResponseDTO;
import com.example.gestox.service.UserService.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private IUserService userService;

    @PostMapping("/add")
    public ResponseEntity<UserResponseDTO> createUser(@ModelAttribute UserRequestDTO userRequestDTO) throws IOException {
        UserResponseDTO userResponse = userService.createUser(userRequestDTO);
        return ResponseEntity.ok(userResponse);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDTO> updateUser(@PathVariable Long id, @ModelAttribute UserRequestDTO userRequestDTO) throws IOException {
        UserResponseDTO updatedUser = userService.updateUser(id, userRequestDTO);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable Long id) {
        UserResponseDTO userResponse = userService.getUserById(id);
        return ResponseEntity.ok(userResponse);
    }

    @GetMapping
    public Page<UserResponseDTO> getUsers(Pageable pageable) {
        return userService.getUsers(pageable);
    }

    @GetMapping("/showAll")
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        List<UserResponseDTO> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }


    @GetMapping("/generate/{userId}")
    public ResponseEntity<byte[]> generatePdf(@PathVariable Long userId) {
        try {
            byte[] pdfBytes  =userService.generatePdf(userId);

            // Return the generated PDF as a response
            HttpHeaders headers = new HttpHeaders();
            headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=ORDER_" + userId + ".pdf");
            headers.setContentType(MediaType.APPLICATION_PDF);

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(pdfBytes);

        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }
}

