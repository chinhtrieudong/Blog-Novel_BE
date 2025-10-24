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

                if ("dummy".equals(cloudName) || "dummy".equals(apiKey) || "dummy".equals(apiSecret)) {
                    // Log warning but don't throw exception to allow application startup
                    System.err.println(
                            "WARNING: Cloudinary credentials are not configured. Please set the CLOUDINARY_URL environment variable with valid credentials.");
                    System.err.println("Using dummy credentials - Cloudinary functionality will not work properly.");
                }

                config.put("cloud_name", cloudName);
                config.put("api_key", apiKey);
                config.put("api_secret", apiSecret);
            } catch (URISyntaxException e) {
                throw new IllegalArgumentException(
                        "Invalid Cloudinary URL format. Please check the CLOUDINARY_URL environment variable.", e);
            }
        } else {
            throw new IllegalArgumentException(
                    "Cloudinary URL must start with 'cloudinary://'. Please set the CLOUDINARY_URL environment variable.");
        }

        return new Cloudinary(config);
    }
}
