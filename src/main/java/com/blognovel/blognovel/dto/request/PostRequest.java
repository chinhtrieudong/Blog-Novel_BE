package com.blognovel.blognovel.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;

@Data
public class PostRequest {
    @NotBlank(message = "Title cannot be blank")
    @Size(min = 5, max = 255, message = "Title must be between 5 and 255 characters")
    private String title;

    @NotBlank(message = "Content cannot be blank")
    @Size(min = 10, message = "Content must be at least 10 characters")
    private String content;

    private MultipartFile coverImage;

    private Long authorId;

    private String status;

    private Set<Long> categoryIds;

    private Set<Long> tagIds;
}
