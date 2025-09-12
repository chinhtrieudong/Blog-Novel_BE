package com.blognovel.blognovel.service;

import org.springframework.web.multipart.MultipartFile;

public interface CloudinaryService {
    String uploadImage(MultipartFile image) throws java.io.IOException;

    String deleteImage(String publicId) throws java.io.IOException;
}
