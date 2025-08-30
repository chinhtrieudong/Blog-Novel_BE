package com.blognovel.blognovel.controller;

import com.blognovel.blognovel.dto.request.UserRequest;
import com.blognovel.blognovel.dto.response.ApiResponse;
import com.blognovel.blognovel.dto.response.UserResponse;
import com.blognovel.blognovel.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public ApiResponse<List<UserResponse>> getAllUsers() {
        return ApiResponse.<List<UserResponse>>builder()
                .code(200)
                .message("All users retrieved successfully")
                .data(userService.getAllUsers())
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<UserResponse> getUserById(@PathVariable Long id) {
        return ApiResponse.<UserResponse>builder()
                .code(200)
                .message("User retrieved successfully")
                .data(userService.getUserById(id))
                .build();
    }

    @PutMapping("/{id}")
    public ApiResponse<UserResponse> updateUser(@PathVariable Long id, @RequestBody UserRequest request) {
        return ApiResponse.<UserResponse>builder()
                .code(200)
                .message("User updated successfully")
                .data(userService.updateUser(id, request))
                .build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ApiResponse.<Void>builder()
                .code(200)
                .message("User deleted successfully")
                .build();
    }

    @PutMapping("/{id}/status")
    public ApiResponse<UserResponse> changeUserStatus(@PathVariable Long id,@RequestParam String status) {
        return ApiResponse.<UserResponse>builder()
                .code(200)
                .message("User status changed successfully")
                .data(userService.changeUserStatus(id, status))
                .build();
    }

    @PutMapping("/{id}/role")
    public ApiResponse<UserResponse> changeUserRole(@PathVariable Long id, @RequestParam String role) {
        return ApiResponse.<UserResponse>builder()
                .code(200)
                .message("User role changed successfully")
                .data(userService.changeUserRole(id, role))
                .build();
    }
}
