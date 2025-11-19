package com.blognovel.blognovel.service.util;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import java.util.concurrent.TimeUnit;

import com.blognovel.blognovel.service.util.UpstashRedisService;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class TokenBlacklistService {

    @Autowired(required = false)
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired(required = false)
    private UpstashRedisService upstashRedisService;

    private static final String BLACKLIST_PREFIX = "blacklist:";

    public void blacklistToken(String token, long expirationMillis) {
        long ttlSeconds = (expirationMillis + 999) / 1000; // convert to seconds
        if (upstashRedisService != null) {
            try {
                upstashRedisService.set(BLACKLIST_PREFIX + token, "true", ttlSeconds);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (redisTemplate != null) {
            String key = BLACKLIST_PREFIX + token;
            redisTemplate.opsForValue().set(key, "true", expirationMillis, TimeUnit.MILLISECONDS);
        }
    }

    public boolean isTokenBlacklisted(String token) {
        if (upstashRedisService != null) {
            try {
                return upstashRedisService.exists(BLACKLIST_PREFIX + token);
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        } else if (redisTemplate == null) {
            return false;
        }
        String key = BLACKLIST_PREFIX + token;
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }
}
