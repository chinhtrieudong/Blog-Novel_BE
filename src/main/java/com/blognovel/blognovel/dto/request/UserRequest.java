package com.blognovel.blognovel.dto.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRequest {
    private String username;
    private String email;
    private String fullName;
    private String bio;
    private String avatarUrl;
}
