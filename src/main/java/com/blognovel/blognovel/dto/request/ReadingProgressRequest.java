package com.blognovel.blognovel.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReadingProgressRequest {
    @NotNull(message = "Read chapters cannot be null")
    @Min(value = 0, message = "Read chapters must be non-negative")
    private Integer readChapters;

    private String lastRead;
}
