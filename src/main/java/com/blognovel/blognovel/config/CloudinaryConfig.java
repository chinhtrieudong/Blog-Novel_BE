package com.blognovel.blognovel.config;

import com.cloudinary.Cloudinary;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class CloudinaryConfig {

    @Value("${cloudinary.url}")
    private String cloudinaryUrl;

    @Bean
    public Cloudinary cloudinary() {
        Map<String, String> config = new HashMap<>();
        if (cloudinaryUrl.startsWith("cloudinary://")) {
            try {
                URI cloudinaryUri = new URI(cloudinaryUrl);
                String[] userInfo = cloudinaryUri.getUserInfo().split(":");
                String apiKey = userInfo[0];
                String apiSecret = userInfo[1];
                String cloudName = cloudinaryUri.getHost();

                config.put("cloud_name", cloudName);
                config.put("api_key", apiKey);
                config.put("api_secret", apiSecret);
            } catch (URISyntaxException e) {
                // For dummy URLs, set dummy values
                config.put("cloud_name", "dummy");
                config.put("api_key", "dummy");
                config.put("api_secret", "dummy");
            }
        } else {
            // Fallback for dummy
            config.put("cloud_name", "dummy");
            config.put("api_key", "dummy");
            config.put("api_secret", "dummy");
        }

        return new Cloudinary(config);
    }
}
