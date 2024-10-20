package com.example.gestox.service.UserService;

import com.example.gestox.dto.UserRequestDTO;
import com.example.gestox.dto.UserResponseDTO;
import com.example.gestox.entity.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.io.IOException;
import java.util.List;

public interface IUserService {
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;

    public UserDetails loadUserByEmail(String email) throws UsernameNotFoundException;

    public List<User> getUsers();

    UserResponseDTO createUser(UserRequestDTO userRequestDTO);
    UserResponseDTO updateUser(Long id, UserRequestDTO userRequestDTO);
    void deleteUser(Long id);
    UserResponseDTO getUserById(Long id);
    List<UserResponseDTO> getAllUsers();

    public byte[] generatePdf(Long id) throws IOException;
}
