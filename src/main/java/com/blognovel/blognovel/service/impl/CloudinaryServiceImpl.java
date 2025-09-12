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

    @Override
    public String uploadImage(MultipartFile image) throws IOException {
        try {
            Map<?, ?> uploadResult = cloudinary.uploader().upload(image.getBytes(), ObjectUtils.asMap(
                    "transformation", new Transformation().width(600).height(400).crop("fill")
            ));
            return uploadResult.get("secure_url").toString();
        } catch (IOException e) {
            throw new AppException(ErrorCode.UPLOAD_FAILED);
        }
    }

    @Override
    public String deleteImage(String publicId) throws IOException {
        try {
            Map<?, ?> destroyResult = cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
            return destroyResult.get("result").toString();
        } catch (IOException e) {
            throw new AppException(ErrorCode.UPLOAD_FAILED);
        }
    }
}