package com.blognovel.blognovel.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class SavedNovelsResponse {
    private Long saved;
    private Long completed;
    private Long reading;
    private Long unread;
    private List<NovelResponse> novels;
}
