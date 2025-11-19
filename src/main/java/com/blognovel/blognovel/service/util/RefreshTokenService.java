package com.blognovel.blognovel.service.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

import com.blognovel.blognovel.service.util.UpstashRedisService;

@Service
public class RefreshTokenService {

    @Autowired(required = false)
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired(required = false)
    private UpstashRedisService upstashRedisService;

    private static final String REFRESH_PREFIX = "refresh:";

    public void saveRefreshToken(String username, String refreshToken, long expirationDays) {
        long ttlSeconds = expirationDays * 24 * 60 * 60; // convert days to seconds
        if (upstashRedisService != null) {
            try {
                upstashRedisService.set(REFRESH_PREFIX + username, refreshToken, ttlSeconds);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (redisTemplate != null) {
            String key = REFRESH_PREFIX + username;
            redisTemplate.opsForValue().set(key, refreshToken, expirationDays, TimeUnit.DAYS);
        }
    }

    public String getRefreshToken(String username) {
        if (upstashRedisService != null) {
            try {
                return upstashRedisService.get(REFRESH_PREFIX + username);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        } else if (redisTemplate == null) {
            return null;
        }
        return (String) redisTemplate.opsForValue().get(REFRESH_PREFIX + username);
    }

    public void deleteRefreshToken(String username) {
        if (upstashRedisService != null) {
            try {
                upstashRedisService.delete(REFRESH_PREFIX + username);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (redisTemplate != null) {
            redisTemplate.delete(REFRESH_PREFIX + username);
        }
    }
}
