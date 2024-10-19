package com.example.gestox.controller;

import com.example.gestox.dto.UserRegistrationDto;
import com.example.gestox.entity.User;
import com.example.gestox.service.UserService.UserServiceImpl;
import com.example.gestox.utility.JwtHelper;
import com.example.gestox.dto.JwtRequest;
import com.example.gestox.dto.JwtResponse;
import com.example.gestox.service.UserService.IUserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private IUserService userDetailsService;
    @Autowired
    private AuthenticationManager manager;
    @Autowired
    private UserServiceImpl userService;
    @Autowired
    private JwtHelper helper;
    private Logger logger = LoggerFactory.getLogger(AuthController.class);

    @PostMapping("/register")
//    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<User> registerUser(@Valid @ModelAttribute UserRegistrationDto registrationDto) throws IOException {
        User createdUser = userService.createUser(registrationDto);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody JwtRequest request) {
        this.doAuthenticate(request.getEmail(), request.getPassword());
        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());
        String token = this.helper.generateToken(userDetails);

        JwtResponse response = JwtResponse.builder()
                .jwtToken(token)
                .username(userDetails.getUsername())
                .role(userDetails.getAuthorities().toString()).build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    private void doAuthenticate(String email, String password) {
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(email, password);
        try {
            manager.authenticate(authentication);
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException(" Invalid Email or Password  !!");
        }
    }

    @ExceptionHandler(BadCredentialsException.class)
    public String exceptionHandler() {
        return "Credentials Invalid !!";
    }

    @GetMapping("/logout")
    private ResponseEntity<?> logout(HttpServletRequest request) {
        return ResponseEntity.ok().body("User logged out successfully");
    }

}