package com.blognovel.blognovel.controller;

import com.blognovel.blognovel.dto.request.UserRequest;
import com.blognovel.blognovel.dto.response.ApiResponse;
import com.blognovel.blognovel.dto.response.UserResponse;
import com.blognovel.blognovel.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "Users", description = "Endpoints for managing user accounts")
public class UserController {

    private final UserService userService;

    @GetMapping
    @Operation(summary = "Get all users", description = "Retrieves a list of all user accounts.")
    public ApiResponse<List<UserResponse>> getAllUsers() {
        return ApiResponse.<List<UserResponse>>builder()
                .code(200)
                .message("All users retrieved successfully")
                .data(userService.getAllUsers())
                .build();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get user by ID", description = "Retrieves a specific user account by its unique identifier.")
    public ApiResponse<UserResponse> getUserById(@Parameter(description = "User ID") @PathVariable Long id) {
        return ApiResponse.<UserResponse>builder()
                .code(200)
                .message("User retrieved successfully")
                .data(userService.getUserById(id))
                .build();
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update user", description = "Updates an existing user account.")
    public ApiResponse<UserResponse> updateUser(
            @Parameter(description = "User ID") @PathVariable Long id,
            @RequestBody UserRequest request) {
        return ApiResponse.<UserResponse>builder()
                .code(200)
                .message("User updated successfully")
                .data(userService.updateUser(id, request))
                .build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete user", description = "Deletes a user account.")
    public ApiResponse<Void> deleteUser(@Parameter(description = "User ID") @PathVariable Long id) {
        userService.deleteUser(id);
        return ApiResponse.<Void>builder()
                .code(200)
                .message("User deleted successfully")
                .build();
    }

    @PutMapping("/{id}/status")
    @Operation(summary = "Change user status", description = "Changes the status of a user account (e.g., active, inactive).")
    public ApiResponse<UserResponse> changeUserStatus(
            @Parameter(description = "User ID") @PathVariable Long id,
            @Parameter(description = "New status for the user") @RequestParam String status) {
        return ApiResponse.<UserResponse>builder()
                .code(200)
                .message("User status changed successfully")
                .data(userService.changeUserStatus(id, status))
                .build();
    }

    @PutMapping("/{id}/role")
    @Operation(summary = "Change user role", description = "Changes the role of a user account (e.g., admin, user).")
    public ApiResponse<UserResponse> changeUserRole(
            @Parameter(description = "User ID") @PathVariable Long id,
            @Parameter(description = "New role for the user") @RequestParam String role) {
        return ApiResponse.<UserResponse>builder()
                .code(200)
                .message("User role changed successfully")
                .data(userService.changeUserRole(id, role))
                .build();
    }
}