package com.blognovel.blognovel.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class UserReadingProgressResponse {
    private Long totalSaved;
    private Long completed;
    private Long reading;
    private Long unread;
    private List<ReadingProgressResponse> progressList;
}
