package com.blognovel.blognovel.controller;

import com.blognovel.blognovel.dto.request.AuthRequest;
import com.blognovel.blognovel.dto.request.ForgotPasswordRequest;
import com.blognovel.blognovel.dto.request.RefreshTokenRequest;
import com.blognovel.blognovel.dto.request.ResetPasswordRequest;
import com.blognovel.blognovel.dto.request.UserRequest;
import com.blognovel.blognovel.dto.response.ApiResponse;
import com.blognovel.blognovel.dto.response.AuthResponse;
import com.blognovel.blognovel.dto.response.TokenResponse;
import com.blognovel.blognovel.dto.response.UserResponse;
import com.blognovel.blognovel.jwt.JwtUtil;
import com.blognovel.blognovel.service.AuthService;
import com.blognovel.blognovel.service.util.RefreshTokenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Authentication related endpoints")
public class AuthController {

    private final AuthService authService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final RefreshTokenService refreshTokenService;

    @PostMapping("/register")
    @Operation(summary = "Register a new user", description = "Registers a new user account.")
    public ApiResponse<UserResponse> register(@RequestBody @Valid AuthRequest request) {
        return ApiResponse.<UserResponse>builder()
                .code(201)
                .message("User registered successfully")
                .data(authService.register(request))
                .build();
    }

    @PostMapping("/login")
    @Operation(summary = "Login", description = "Authenticates user and returns access and refresh tokens.")
    public ApiResponse<AuthResponse> login(@RequestBody @Valid AuthRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        final String accessToken = jwtUtil.generateToken(request.getUsername());
        final String refreshToken = jwtUtil.generateRefreshToken(request.getUsername());
        refreshTokenService.saveRefreshToken(request.getUsername(), refreshToken, 7);
        return ApiResponse.<AuthResponse>builder()
                .code(200)
                .message("Login successful")
                .data(new AuthResponse(accessToken, refreshToken))
                .build();
    }

    @PostMapping("/logout")
    @Operation(summary = "Logout", description = "Invalidates the provided token.")
    public ApiResponse<String> logout(@RequestHeader("Authorization") String token) {
        authService.logout(token);
        String username = jwtUtil.extractUsername(token);
        refreshTokenService.deleteRefreshToken(username);
        return ApiResponse.<String>builder()
                .code(200)
                .message("Logout successful")
                .data("Token invalidated on client side")
                .build();
    }

    @GetMapping("/me")
    @Operation(summary = "Get current user", description = "Retrieves the currently authenticated user's profile.")
    public ApiResponse<UserResponse> getCurrentUser(Authentication authentication) {
        String username = authentication.getName();
        return ApiResponse.<UserResponse>builder()
                .code(200)
                .message("Current user fetched successfully")
                .data(authService.getProfile(username))
                .build();
    }

    @PutMapping("/profile")
    @Operation(summary = "Update profile", description = "Updates the currently authenticated user's profile.")
    public ApiResponse<UserResponse> updateProfile(@RequestBody @Valid UserRequest request, Authentication authentication) {
        String username = authentication.getName();
        return ApiResponse.<UserResponse>builder()
                .code(200)
                .message("User profile updated successfully")
                .data(authService.updateProfile(username, request))
                .build();
    }

    @PostMapping("/forgot-password")
    @Operation(summary = "Forgot password", description = "Sends a reset password link to the user's email if the email exists.")
    public ApiResponse<String> forgotPassword(@RequestBody @Valid ForgotPasswordRequest request) {
        authService.forgotPassword(request);
        return ApiResponse.<String>builder()
                .code(200)
                .message("If the email exists, a reset link has been sent.")
                .data("Check your email for reset instructions.")
                .build();
    }

    @PostMapping("/reset-password")
    @Operation(summary = "Reset password", description = "Resets the user's password using the provided token and new password.")
    public ApiResponse<String> resetPassword(@RequestBody @Valid ResetPasswordRequest request) {
        authService.resetPassword(request);
        return ApiResponse.<String>builder()
                .code(200)
                .message("Password reset successfully.")
                .data("You can now login with your new password.")
                .build();
    }

    @PostMapping("/refresh-token")
    @Operation(summary = "Refresh token", description = "Refreshes the access token using a valid refresh token.")
    public ApiResponse<TokenResponse> refreshToken(@RequestBody @Valid RefreshTokenRequest request) {
        String username = jwtUtil.extractUsername(request.getRefreshToken());
        String storedToken = refreshTokenService.getRefreshToken(username);
        if (storedToken == null || !storedToken.equals(request.getRefreshToken())) {
            throw new RuntimeException("Invalid refresh token");
        }
        String newAccessToken = jwtUtil.generateToken(username);
        String newRefreshToken = jwtUtil.generateRefreshToken(username);
        refreshTokenService.saveRefreshToken(username, newRefreshToken, 7);
        return ApiResponse.<TokenResponse>builder()
                .code(200)
                .message("Token refreshed successfully")
                .data(new TokenResponse(newAccessToken, newRefreshToken))
                .build();
    }
}