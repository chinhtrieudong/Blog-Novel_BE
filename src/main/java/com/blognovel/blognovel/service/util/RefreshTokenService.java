package com.blognovel.blognovel.service.util;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RedisTemplate<String, Object> redisTemplate;
    private static final String REFRESH_PREFIX = "refresh:";

    public void saveRefreshToken(String username, String refreshToken, long expirationDays) {
        String key = REFRESH_PREFIX + username;
        redisTemplate.opsForValue().set(key, refreshToken, expirationDays, TimeUnit.DAYS);
    }

    public String getRefreshToken(String username) {
        return (String) redisTemplate.opsForValue().get(REFRESH_PREFIX + username);
    }

    public void deleteRefreshToken(String username) {
        redisTemplate.delete(REFRESH_PREFIX + username);
    }
}
