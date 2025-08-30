package com.blognovel.blognovel.controller;

import com.blognovel.blognovel.dto.request.AuthRequest;
import com.blognovel.blognovel.dto.request.UserRequest;
import com.blognovel.blognovel.dto.response.ApiResponse;
import com.blognovel.blognovel.dto.response.AuthResponse;
import com.blognovel.blognovel.dto.response.UserResponse;
import com.blognovel.blognovel.jwt.JwtUtil;
import com.blognovel.blognovel.service.AuthService;
import com.blognovel.blognovel.service.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;

    @PostMapping("/register")
    public ApiResponse<UserResponse> register(@RequestBody AuthRequest request) {
        return ApiResponse.<UserResponse>builder()
                .code(201)
                .message("User registered successfully")
                .data(authService.register(request))
                .build();
    }

    @PostMapping("/login")
    public ApiResponse<AuthResponse> login(@RequestBody AuthRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        final UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());

        final String token = jwtUtil.generateToken(userDetails);

        return ApiResponse.<AuthResponse>builder()
                .code(200)
                .message("Login successful")
                .data(new AuthResponse(token))
                .build();
    }
}
