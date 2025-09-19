package com.blognovel.blognovel.controller;

import com.blognovel.blognovel.dto.response.ApiResponse;
import com.blognovel.blognovel.service.CloudinaryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/uploads")
@RequiredArgsConstructor
@Tag(name = "File Uploads", description = "Endpoints for uploading and deleting files")
public class FileUploadController {

    private final CloudinaryService cloudinaryService;

    @PostMapping(
            value = "/novels/cover-image",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    @Operation(summary = "Upload novel cover image", description = "Uploads a cover image for a novel to Cloudinary")
    public ResponseEntity<ApiResponse<String>> uploadNovelCoverImage(
            @Parameter(description = "File to upload", required = true,
                    content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE,
                            schema = @Schema(type = "string", format = "binary")))
            @RequestPart("image") MultipartFile image
    ) {
        try {
            String imageUrl = cloudinaryService.uploadImage(image);
            ApiResponse<String> response = ApiResponse.<String>builder()
                    .code(HttpStatus.OK.value())
                    .message("Image uploaded successfully")
                    .data(imageUrl)
                    .build();
            return ResponseEntity.ok(response);
        } catch (IOException e) {
            ApiResponse<String> response = ApiResponse.<String>builder()
                    .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message("Failed to upload image: " + e.getMessage())
                    .build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @DeleteMapping("/novels/cover-image")
    @Operation(summary = "Delete novel cover image", description = "Deletes a novel cover image from Cloudinary")
    public ApiResponse<String> deleteNovelCoverImage(@Parameter(description = "Public ID of the image to delete", required = true) @RequestParam("publicId") String publicId) {
        try {
            String result = cloudinaryService.deleteImage(publicId);
            return ApiResponse.<String>builder()
                    .code(200)
                    .message("Image deleted successfully")
                    .data(result)
                    .build();
        } catch (IOException e) {
            return ApiResponse.<String>builder()
                    .code(500)
                    .message("Failed to delete image: " + e.getMessage())
                    .build();
        }
    }
}