package com.example.gestox.service.UserService;


import com.example.gestox.dao.ConfirmationTokenRepository;
import com.example.gestox.dao.FileStorageRepository;
import com.example.gestox.dao.UserRepository;
import com.example.gestox.dto.ChangePasswordRequest;
import com.example.gestox.dto.UserRegistrationDto;
import com.example.gestox.dto.UserRequestDTO;
import com.example.gestox.dto.UserResponseDTO;
import com.example.gestox.entity.ConfirmationToken;
import com.example.gestox.entity.FileStorage;
import com.example.gestox.entity.User;
import com.example.gestox.service.EmailService.EmailService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserServiceImpl implements IUserService, UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    private UserRepository userRepository;

    private PasswordEncoder passwordEncoder;

    private FileStorageRepository fileStorageRepository;

    private ConfirmationTokenRepository confirmationTokenRepository;

    @Autowired
    private EmailService emailService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> optionalUser = userRepository.findByFirstName(username);
        if (!optionalUser.isPresent()) {
            optionalUser = userRepository.findByEmailAddress(username);
            if (!optionalUser.isPresent())
                throw new UsernameNotFoundException("User not found with username or email : " + username);
        }
        User user = optionalUser.orElse(null);
        return new org.springframework.security.core.userdetails.User(user.getFirstName(), user.getPassword(),
                getAuthorities(user));
    }

    @Override
    public UserDetails loadUserByEmail(String email) throws UsernameNotFoundException {
        Optional<User> optionalUser = userRepository.findByEmailAddress(email);
        if (!optionalUser.isPresent()) {
            throw new UsernameNotFoundException("User not found with email: " + email);
        }
        User user = optionalUser.orElse(null);
        return new org.springframework.security.core.userdetails.User(user.getFirstName(), user.getPassword(),
                getAuthorities(user));
    }

    private Collection<? extends GrantedAuthority> getAuthorities(User user) {
        return Arrays.stream(user.getRole().split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }


    @Override
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    public User createUser(UserRegistrationDto registrationDto) throws IOException {
        Optional<User> optionalUser = userRepository.findByEmailAddress(registrationDto.getEmail());
        if (!optionalUser.isEmpty()) {
            throw new RuntimeException("email already in use");
        }
        User user = new User();
        user.setAddress(registrationDto.getEmail());
        user.setEnabled(true);
        user.setFirstName(registrationDto.getFirstName());
        user.setLastAccess(null);
        user.setLastName(registrationDto.getLastName());
        user.setPassword(passwordEncoder.encode(registrationDto.getPassword()));
        user.setPhoneNumber(registrationDto.getPhoneNumber());
        user.setRole("ROLE_ADMIN");
        user.setStatus(registrationDto.getStatus());
        FileStorage profileImg = new FileStorage();
        profileImg.setFileName(registrationDto.getProfileImage().getOriginalFilename());
        profileImg.setFileType(registrationDto.getProfileImage().getContentType());
        profileImg.setData(registrationDto.getProfileImage().getBytes());
        profileImg.setCreationDate(LocalDateTime.now());
        fileStorageRepository.save(profileImg);
        user.setProfileImage(profileImg);
        User savedUser = userRepository.save(user);
        return savedUser;
    }

    @Transactional
    public String confirmEmailToken(String token) {
        ConfirmationToken confirmationToken = confirmationTokenRepository
                .findByToken(token)
                .orElseThrow(() -> new IllegalStateException("Token not found"));

        if (confirmationToken.getConfirmedAt() != null) {
            throw new IllegalStateException("Email already confirmed");
        }

        LocalDateTime expiredAt = confirmationToken.getExpiresAt();

        if (expiredAt.isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("Token expired");
        }

        confirmationToken.setConfirmedAt(LocalDateTime.now());
        confirmationTokenRepository.save(confirmationToken);

        User user = confirmationToken.getUser();
        user.setEnabled(true);
        userRepository.save(user);

        return "Confirmed";
    }

    private String buildEmail(String name, LocalDateTime dateTime) {
        return "Dear " + name + "\n\n" +
                "You have changed your password at :\n" + dateTime + "\n \nBest regards";
    }

    public void changePassword(String pageName) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<User> optionalUser = userRepository.findByEmailAddress(userDetails.getUsername());

        if (!optionalUser.isPresent()) {
            throw new UsernameNotFoundException("User not found with username: " + userDetails.getUsername());
        }
        User user = optionalUser.get();
        String link = pageName;
        try {
            emailService.sendEmail("ghassenbayachatti@gmail.com",
                    user.getAddress(),
                    "Change Password",
                    buildEmail(user.getFirstName(), LocalDateTime.now()),
                    null
            );
        } catch (IOException e) {
            logger.error(e.getMessage());
        } catch (javax.mail.MessagingException e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }

    }

    public ResponseEntity<String> confirmChangePassword(ChangePasswordRequest changePasswordRequest) {
        try {
            UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            Optional<User> optionalUser = userRepository.findByEmailAddress(userDetails.getUsername());
            if (!optionalUser.isPresent()) {
                return new ResponseEntity<>("User not found with username: " + userDetails.getUsername(), HttpStatus.NOT_FOUND);
            }
            User user = optionalUser.orElse(null);

            if (!passwordEncoder.matches(changePasswordRequest.getOldPassword(), user.getPassword())) {
                return new ResponseEntity<>("Old password is incorrect", HttpStatus.BAD_REQUEST);
            }

            if (!changePasswordRequest.getNewPassword().equals(changePasswordRequest.getNewPasswordConfirmation())) {
                return new ResponseEntity<>("password and password confirmation do not match", HttpStatus.BAD_REQUEST);
            }

            if (passwordEncoder.matches(changePasswordRequest.getNewPassword(), user.getPassword())) {
                return new ResponseEntity<>("old password and new password are the same", HttpStatus.BAD_REQUEST);
            }

            user.setPassword(passwordEncoder.encode(changePasswordRequest.getNewPassword()));
            userRepository.save(user);

            try {
                emailService.sendEmail("ghassenbayachatti@gmail.com",
                        user.getAddress(),
                        "Password Change",
                        buildEmail(user.getFirstName(), LocalDateTime.now()),
                        null
                );
            } catch (IOException | javax.mail.MessagingException e) {
                logger.error(e.getMessage());
                return new ResponseEntity<>("Error sending email notification", HttpStatus.INTERNAL_SERVER_ERROR);
            }

            return new ResponseEntity<>("Password Changed Successfully", HttpStatus.OK);
        } catch (Exception e) {
            // General error handling
            logger.error(e.getMessage(), e);
            return new ResponseEntity<>("An error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public UserResponseDTO createUser(UserRequestDTO userRequestDTO) {
        FileStorage profileImage = null;
        if (userRequestDTO.getProfileImageId() != null) {
            profileImage = fileStorageRepository.findById(userRequestDTO.getProfileImageId())
                    .orElseThrow(() -> new RuntimeException("Profile image not found with id: " + userRequestDTO.getProfileImageId()));
        }

        User user = mapToEntity(userRequestDTO, profileImage);
        User savedUser = userRepository.save(user);
        return mapToResponseDTO(savedUser);
    }

    @Override
    public UserResponseDTO updateUser(Long id, UserRequestDTO userRequestDTO) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

        FileStorage profileImage = null;
        if (userRequestDTO.getProfileImageId() != null) {
            profileImage = fileStorageRepository.findById(userRequestDTO.getProfileImageId())
                    .orElseThrow(() -> new RuntimeException("Profile image not found with id: " + userRequestDTO.getProfileImageId()));
        }

        user.setFirstName(userRequestDTO.getFirstName());
        user.setLastName(userRequestDTO.getLastName());
        user.setAddress(userRequestDTO.getAddress());
        user.setPhoneNumber(userRequestDTO.getPhoneNumber());
        user.setPassword(userRequestDTO.getPassword());
        user.setRole(userRequestDTO.getRole());
        user.setStatus(userRequestDTO.getStatus());
        user.setLastAccess(userRequestDTO.getLastAccess());
        user.setEnabled(userRequestDTO.isEnabled());
        user.setProfileImage(profileImage);

        User updatedUser = userRepository.save(user);
        return mapToResponseDTO(updatedUser);
    }

    @Override
    public void deleteUser(Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
        } else {
            throw new RuntimeException("User not found with id: " + id);
        }
    }

    @Override
    public UserResponseDTO getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        return mapToResponseDTO(user);
    }

    @Override
    public List<UserResponseDTO> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    private User mapToEntity(UserRequestDTO userRequestDTO, FileStorage profileImage) {
        return User.builder()
                .firstName(userRequestDTO.getFirstName())
                .lastName(userRequestDTO.getLastName())
                .address(userRequestDTO.getAddress())
                .phoneNumber(userRequestDTO.getPhoneNumber())
                .password(userRequestDTO.getPassword())
                .role(userRequestDTO.getRole())
                .status(userRequestDTO.getStatus())
                .lastAccess(userRequestDTO.getLastAccess())
                .enabled(userRequestDTO.isEnabled())
                .profileImage(profileImage)
                .build();
    }

    private UserResponseDTO mapToResponseDTO(User user) {
        String profileImageUrl = user.getProfileImage() != null ? "/api/files/" + user.getProfileImage().getId() : null;

        return UserResponseDTO.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .address(user.getAddress())
                .phoneNumber(user.getPhoneNumber())
                .role(user.getRole())
                .status(user.getStatus())
                .lastAccess(user.getLastAccess())
                .enabled(user.isEnabled())
                .profileImageUrl(profileImageUrl)
                .build();
    }
}
