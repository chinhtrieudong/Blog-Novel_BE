package com.blognovel.blognovel.service;

import com.blognovel.blognovel.dto.request.AuthRequest;
import com.blognovel.blognovel.dto.request.ForgotPasswordRequest;
import com.blognovel.blognovel.dto.request.ResetPasswordRequest;
import com.blognovel.blognovel.dto.request.UserRequest;
import com.blognovel.blognovel.dto.response.TokenResponse;
import com.blognovel.blognovel.dto.response.UserResponse;
import org.springframework.stereotype.Service;

@Service
public interface AuthService {
    UserResponse register(AuthRequest request);

    void logout(String token);

    UserResponse getProfile(String username);

    UserResponse updateProfile(String username, UserRequest request);

    void forgotPassword(ForgotPasswordRequest request);

    void resetPassword(ResetPasswordRequest request);

    TokenResponse refreshToken(String refreshToken);
}
