package com.blognovel.blognovel.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApiResponse<T> {
    @Builder.Default
    private int code = 200;

    @Builder.Default
    private String message = "Success";

    private T data;
}