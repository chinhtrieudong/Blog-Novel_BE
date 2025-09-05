package com.blognovel.blognovel.service;

import com.blognovel.blognovel.dto.request.UserRequest;
import com.blognovel.blognovel.dto.response.UserResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {
    List<UserResponse> getAllUsers();

    UserResponse getUserById(Long id);

    UserResponse updateUser(Long id, UserRequest request);

    void deleteUser(Long id);

    UserResponse changeUserStatus(Long id, String status);

    UserResponse changeUserRole(Long id, String role);

    UserResponse getCurrentUser(String username);
}
