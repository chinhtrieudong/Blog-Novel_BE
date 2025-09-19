package com.blognovel.blognovel.service;

import com.blognovel.blognovel.dto.request.ChapterRequest;
import com.blognovel.blognovel.dto.response.ChapterResponse;

import java.util.List;

public interface ChapterService {
    List<ChapterResponse> getAllChaptersByNovelId(Long novelId);
    ChapterResponse getChapterById(Long novelId, Long chapterId);
    ChapterResponse createChapter(Long novelId, ChapterRequest chapterRequest);
    ChapterResponse updateChapter(Long novelId, Long chapterId, ChapterRequest chapterRequest);
    void deleteChapter(Long novelId, Long chapterId);
    void incrementViewCount(Long novelId, Long chapterId);
}