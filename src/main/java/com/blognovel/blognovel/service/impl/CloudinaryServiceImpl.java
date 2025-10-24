package com.blognovel.blognovel.service.impl;

import com.blognovel.blognovel.exception.AppException;
import com.blognovel.blognovel.exception.ErrorCode;
import com.blognovel.blognovel.service.CloudinaryService;
import com.cloudinary.Cloudinary;
import com.cloudinary.Transformation;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CloudinaryServiceImpl implements CloudinaryService {

    private final Cloudinary cloudinary;

    private boolean isUsingDummyCredentials() {
        // Check if we're using dummy credentials by examining the cloudinary URL
        return cloudinary.toString().contains("dummy");
    }

    @Override
    public String uploadImage(MultipartFile image) throws IOException {
        if (image == null || image.isEmpty()) {
            throw new IllegalArgumentException("Image file is required and cannot be empty.");
        }

        // Check if using dummy credentials
        if (isUsingDummyCredentials()) {
            throw new AppException(ErrorCode.CLOUDINARY_ERROR);
        }

        // Validate file type
        String contentType = image.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new AppException(ErrorCode.INVALID_FILE_FORMAT);
        }

        // Validate file size (max 10MB)
        if (image.getSize() > 10 * 1024 * 1024) {
            throw new IllegalArgumentException("File size must be less than 10MB.");
        }

        try {
            System.out.println("Starting Cloudinary upload for file: " + image.getOriginalFilename() +
                    ", size: " + image.getSize() + ", type: " + image.getContentType());

            Map<?, ?> uploadResult = cloudinary.uploader().upload(image.getBytes(), ObjectUtils.asMap(
                    "transformation", new Transformation().width(600).height(400).crop("fill")));

            System.out.println("Cloudinary upload successful: " + uploadResult);
            return uploadResult.get("secure_url").toString();
        } catch (IOException e) {
            System.err.println("Cloudinary IO error: " + e.getMessage());
            e.printStackTrace();
            throw new AppException(ErrorCode.CLOUDINARY_ERROR); // Network/IO error
        } catch (Exception e) {
            System.err.println("Cloudinary general error: " + e.getMessage());
            e.printStackTrace();
            throw new AppException(ErrorCode.UPLOAD_FAILED); // General upload failure
        }
    }

    @Override
    public String deleteImage(String publicId) throws IOException {
        // Check if using dummy credentials
        if (isUsingDummyCredentials()) {
            throw new AppException(ErrorCode.CLOUDINARY_ERROR);
        }

        try {
            Map<?, ?> destroyResult = cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
            return destroyResult.get("result").toString();
        } catch (Exception e) {
            throw new AppException(ErrorCode.UPLOAD_FAILED);
        }
    }
}
