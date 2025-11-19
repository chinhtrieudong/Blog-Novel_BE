package com.blognovel.blognovel.config;

import com.blognovel.blognovel.service.util.UpstashRedisService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@EnableCaching
@ConditionalOnProperty(value = "redis.enabled", havingValue = "true", matchIfMissing = true)
public class RedisConfig {

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new StringRedisSerializer());
        return template;
    }

    @ConditionalOnProperty(value = "redis.rest", havingValue = "true")
    @Bean
    public UpstashRedisService upstashRedisService(
            @Value("${UPSTASH_REDIS_REST_URL}") String url,
            @Value("${UPSTASH_REDIS_REST_TOKEN}") String token) {
        return new UpstashRedisService(url, token);
    }
}
