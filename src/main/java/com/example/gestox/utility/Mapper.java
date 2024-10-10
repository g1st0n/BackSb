package com.example.gestox.utility;

import com.example.gestox.dto.FileStorageResponse;
import com.example.gestox.dto.UserRequest;
import com.example.gestox.dto.UserResponse;
import com.example.gestox.entity.FileStorage;
import com.example.gestox.entity.User;

public class Mapper {

    // Convert UserRequest to User entity (for create or update)
    public static User toEntity(UserRequest userRequest, FileStorage profileImage) {
        return User.builder()
                .firstName(userRequest.getFirstName())
                .lastName(userRequest.getLastName())
                .address(userRequest.getAddress())
                .phoneNumber(userRequest.getPhoneNumber())
                .password(userRequest.getPassword())  // Ensure you handle password hashing elsewhere
                .role(userRequest.getRole())
                .enabled(userRequest.isEnabled())
                .profileImage(profileImage)  // Optionally map the profile image
                .build();
    }

    // Convert User entity to UserResponse (for returning data to the frontend)
    public static UserResponse toResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .address(user.getAddress())
                .phoneNumber(user.getPhoneNumber())
                .role(user.getRole())
                .status(user.getStatus())
                .lastAccess(user.getLastAccess())
                .enabled(user.isEnabled())
                .profileImageUrl(user.getProfileImage() != null ? "/images/" + user.getProfileImage().getFileName() : null)
                .profileImageFileName(user.getProfileImage() != null ? user.getProfileImage().getFileName() : null)
                .profileImageFileType(user.getProfileImage() != null ? user.getProfileImage().getFileType() : null)
                .build();
    }

    public static FileStorageResponse toResponse(FileStorage fileStorage) {
        return FileStorageResponse.builder()
                .id(fileStorage.getId())
                .fileName(fileStorage.getFileName())
                .fileType(fileStorage.getFileType())
                .creationDate(fileStorage.getCreationDate())
                .build();
    }
}
