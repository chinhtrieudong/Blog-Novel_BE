package com.blognovel.blognovel.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@EnableCaching
@ConditionalOnProperty(value = "redis.enabled", havingValue = "true", matchIfMissing = true)
public class RedisConfig {

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        try {
            return new LettuceConnectionFactory(); // Spring tự đọc spring.data.redis.url
        } catch (Exception e) {
            System.out.println("⚠ Redis không kết nối được → disable Redis");
            return null;
        }
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        if (connectionFactory == null) {
            System.out.println("⚠ RedisTemplate = null (Redis disabled)");
            return null;
        }
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new StringRedisSerializer());
        return template;
    }
}
