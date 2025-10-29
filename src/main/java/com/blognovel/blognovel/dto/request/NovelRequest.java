package com.blognovel.blognovel.dto.request;

import com.blognovel.blognovel.enums.NovelStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
public class NovelRequest {
    @NotBlank(message = "Title cannot be blank")
    @Size(min = 5, max = 255, message = "Title must be between 5 and 255 characters")
    private String title;

    @NotBlank(message = "Description cannot be blank")
    @Size(min = 10, message = "Description must be at least 10 characters")
    private String description;

    @NotEmpty(message = "Author IDs cannot be empty")
    private List<Long> authorIds;

    @NotEmpty(message = "Genre IDs cannot be empty")
    private List<Long> genreIds;

    private NovelStatus status;

    private MultipartFile coverImage;
}
