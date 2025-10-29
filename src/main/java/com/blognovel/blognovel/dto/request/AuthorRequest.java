package com.blognovel.blognovel.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class AuthorRequest {
    @NotBlank(message = "Author name cannot be blank")
    @Size(min = 2, max = 255, message = "Author name must be between 2 and 255 characters")
    private String name;

    private String bio;

    private MultipartFile avatarImage;
}
