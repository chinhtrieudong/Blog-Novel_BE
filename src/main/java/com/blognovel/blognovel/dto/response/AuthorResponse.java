package com.blognovel.blognovel.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AuthorResponse {
    private Long id;
    private String name;
    private String bio;
    private String avatarUrl;
}
