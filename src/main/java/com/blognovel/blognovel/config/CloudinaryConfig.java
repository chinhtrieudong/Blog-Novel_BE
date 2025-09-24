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

    @Value("${CLOUDINARY_URL}")
    private String cloudinaryUrl;

    @Bean
    public Cloudinary cloudinary() {
        Map<String, String> config = new HashMap<>();
        URI cloudinaryUri;
        try {
            cloudinaryUri = new URI(cloudinaryUrl);
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException("Invalid Cloudinary URL: " + cloudinaryUrl, e);
        }

        String[] userInfo = cloudinaryUri.getUserInfo().split(":");
        String apiKey = userInfo[0];
        String apiSecret = userInfo[1];
        String cloudName = cloudinaryUri.getHost();

        config.put("cloud_name", cloudName);
        config.put("api_key", apiKey);
        config.put("api_secret", apiSecret);

        return new Cloudinary(config);
    }
}
