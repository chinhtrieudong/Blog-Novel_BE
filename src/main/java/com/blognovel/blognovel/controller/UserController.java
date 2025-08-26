package com.blognovel.blognovel.controller;

import com.blognovel.blognovel.dto.request.UserRequest;
import com.blognovel.blognovel.dto.response.ApiResponse;
import com.blognovel.blognovel.dto.response.UserResponse;
import com.blognovel.blognovel.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public ApiResponse<UserResponse> register(@RequestBody UserRequest request) {
        return ApiResponse.<UserResponse>builder()
                .code(201)
                .message("User registered successfully")
                .data(userService.register(request))
                .build();
    }

    @GetMapping("/me/{id}")
    public ApiResponse<UserResponse> getUser(@PathVariable Long id) {
        return ApiResponse.<UserResponse>builder()
                .code(200)
                .message("User retrieved successfully")
                .data(userService.getUser(id))
                .build();
    }
}
