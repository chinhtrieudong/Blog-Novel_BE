package com.blognovel.blognovel.dto.request;

import com.blognovel.blognovel.enums.NovelStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class NovelRequest {

    @NotBlank(message = "Title cannot be blank")
    @Size(max = 255, message = "Title cannot exceed 255 characters")
    private String title;

    @NotBlank(message = "Description cannot be blank")
    private String description;

    @NotNull(message = "Author ID cannot be null")
    private Long authorId;

    @NotNull(message = "Genre IDs cannot be null")
    private Set<Long> genreIds;

    @NotNull(message = "Status cannot be null")
    private NovelStatus status;

    private String coverImage;
}
