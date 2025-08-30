package com.blognovel.blognovel.service;

import com.blognovel.blognovel.dto.request.AuthRequest;
import com.blognovel.blognovel.dto.request.UserRequest;
import com.blognovel.blognovel.dto.response.UserResponse;
import org.springframework.stereotype.Service;

@Service
public interface AuthService {
    UserResponse register(AuthRequest request);
}
