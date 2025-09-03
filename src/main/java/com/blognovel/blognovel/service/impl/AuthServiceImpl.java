package com.blognovel.blognovel.service.impl;

import com.blognovel.blognovel.dto.request.AuthRequest;
import com.blognovel.blognovel.dto.request.ForgotPasswordRequest;
import com.blognovel.blognovel.dto.request.ResetPasswordRequest;
import com.blognovel.blognovel.dto.request.UserRequest;
import com.blognovel.blognovel.dto.response.TokenResponse;
import com.blognovel.blognovel.dto.response.UserResponse;
import com.blognovel.blognovel.enums.Role;
import com.blognovel.blognovel.enums.Status;
import com.blognovel.blognovel.exception.AppException;
import com.blognovel.blognovel.exception.ErrorCode;
import com.blognovel.blognovel.jwt.JwtUtil;
import com.blognovel.blognovel.mapper.UserMapper;
import com.blognovel.blognovel.model.User;
import com.blognovel.blognovel.repository.UserRepository;
import com.blognovel.blognovel.service.AuthService;
import com.blognovel.blognovel.service.util.TokenBlacklistService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final JwtUtil jwtUtil;
    private final TokenBlacklistService blacklistService;
    private final RedisTemplate<String, Object> redisTemplate;
    private static final String RESET_PREFIX = "reset:";

    public UserResponse register(AuthRequest request) {
        if (userRepository.existsByEmail(request.getEmail())
                || userRepository.existsByUsername(request.getUsername())) {
            throw new AppException(ErrorCode.USER_ALREADY_EXISTS);
        }
        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .status(Status.ACTIVE)
                .build();
        return userMapper.toResponse(userRepository.save(user));
    }

    public void logout(String token) {
        String jwt = token.replace("Bearer ", "");
        long expirationMillis = jwtUtil.getExpirationMillis(jwt);
        blacklistService.blacklistToken(jwt, expirationMillis);
    }

    @Override
    public UserResponse getProfile(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        return userMapper.toResponse(user);
    }

    @Override
    public TokenResponse refreshToken(String refreshToken) {
        if (!jwtUtil.validateRefreshToken(refreshToken)) {
            throw new AppException(ErrorCode.INVALID_TOKEN);
        }

        String username = jwtUtil.extractUsername(refreshToken);
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        String newAccessToken = jwtUtil.generateToken(user.getUsername());
        String newRefreshToken = jwtUtil.generateRefreshToken(user.getUsername());

        return new TokenResponse(newAccessToken, newRefreshToken);
    }

    @Override
    public void forgotPassword(ForgotPasswordRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        if (user != null) {
            String token = UUID.randomUUID().toString();
            redisTemplate.opsForValue().set(RESET_PREFIX + token, user.getUsername(), 15, TimeUnit.MINUTES);
            // TODO: Gửi email thực tế, tạm thời log ra console
            System.out.println("Reset password link: http://yourdomain/reset-password?token=" + token);
        }
    }

    @Override
    public void resetPassword(ResetPasswordRequest request) {
        String username = (String) redisTemplate.opsForValue().get(RESET_PREFIX + request.getToken());
        if (username == null) {
            throw new AppException(ErrorCode.INVALID_TOKEN);
        }
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
        redisTemplate.delete(RESET_PREFIX + request.getToken());
    }

    @Override
    public UserResponse updateProfile(String username, UserRequest request) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        user.setUsername(request.getUsername() != null ? request.getUsername() : user.getUsername());
        user.setEmail(request.getEmail() != null ? request.getEmail() : user.getEmail());
        user.setFullName(request.getFullName() != null ? request.getFullName() : user.getFullName());
        user.setBio(request.getBio() != null ? request.getBio() : user.getBio());
        user.setAvatarUrl(request.getAvatarUrl() != null ? request.getAvatarUrl() : user.getAvatarUrl());

        return userMapper.toResponse(userRepository.save(user));
    }
}
