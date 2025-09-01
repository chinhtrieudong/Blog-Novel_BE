package com.blognovel.blognovel.controller;

import com.blognovel.blognovel.dto.request.AuthRequest;
import com.blognovel.blognovel.dto.request.ForgotPasswordRequest;
import com.blognovel.blognovel.dto.request.RefreshTokenRequest;
import com.blognovel.blognovel.dto.request.ResetPasswordRequest;
import com.blognovel.blognovel.dto.response.ApiResponse;
import com.blognovel.blognovel.dto.response.AuthResponse;
import com.blognovel.blognovel.dto.response.TokenResponse;
import com.blognovel.blognovel.dto.response.UserResponse;
import com.blognovel.blognovel.jwt.JwtUtil;
import com.blognovel.blognovel.service.AuthService;
import com.blognovel.blognovel.service.util.RefreshTokenService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final RefreshTokenService refreshTokenService;

    @PostMapping("/register")
    public ApiResponse<UserResponse> register(@RequestBody @Valid AuthRequest request) {

        return ApiResponse.<UserResponse>builder()
                .code(201)
                .message("User registered successfully")
                .data(authService.register(request))
                .build();
    }

    @PostMapping("/login")
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
    public ApiResponse<UserResponse> getCurrentUser(Authentication authentication) {
        String username = authentication.getName();
        return ApiResponse.<UserResponse>builder()
                .code(200)
                .message("Current user fetched successfully")
                .data(authService.getProfile(username))
                .build();
    }

    @PostMapping("/refresh-token")
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

    @PostMapping("/forgot-password")
    public ApiResponse<String> forgotPassword(@RequestBody @Valid ForgotPasswordRequest request) {
        authService.forgotPassword(request);
        return ApiResponse.<String>builder()
                .code(200)
                .message("If the email exists, a reset link has been sent.")
                .data("Check your email for reset instructions.")
                .build();
    }

    @PostMapping("/reset-password")
    public ApiResponse<String> resetPassword(@RequestBody @Valid ResetPasswordRequest request) {
        authService.resetPassword(request);
        return ApiResponse.<String>builder()
                .code(200)
                .message("Password reset successfully.")
                .data("You can now login with your new password.")
                .build();
    }
}
