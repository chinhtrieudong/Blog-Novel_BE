package com.blognovel.blognovel.service.impl;

import com.blognovel.blognovel.dto.request.ChapterRequest;
import com.blognovel.blognovel.dto.response.ChapterResponse;
import com.blognovel.blognovel.mapper.ChapterMapper;
import com.blognovel.blognovel.model.Chapter;
import com.blognovel.blognovel.model.Novel;
import com.blognovel.blognovel.repository.ChapterRepository;
import com.blognovel.blognovel.repository.NovelRepository;
import com.blognovel.blognovel.service.ChapterService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChapterServiceImpl implements ChapterService {

    private final ChapterRepository chapterRepository;
    private final NovelRepository novelRepository;
    private final ChapterMapper chapterMapper;

    @Override
    public List<ChapterResponse> getAllChaptersByNovelId(Long novelId) {
        return chapterRepository.findByNovelId(novelId).stream()
                .map(chapterMapper::chapterToChapterResponse)
                .collect(Collectors.toList());
    }

    @Override
    public ChapterResponse getChapterById(Long novelId, Long chapterId) {
        Chapter chapter = chapterRepository.findByIdAndNovelId(chapterId, novelId)
                .orElseThrow(() -> new EntityNotFoundException("Chapter not found with id " + chapterId + " and novelId " + novelId));
        return chapterMapper.chapterToChapterResponse(chapter);
    }

    @Override
    public ChapterResponse createChapter(Long novelId, ChapterRequest chapterRequest) {
        Novel novel = novelRepository.findById(novelId)
                .orElseThrow(() -> new EntityNotFoundException("Novel not found with id " + novelId));

        Chapter chapter = Chapter.builder()
                .title(chapterRequest.getTitle())
                .content(chapterRequest.getContent())
                .chapterNumber(chapterRequest.getChapterNumber())
                .novel(novel)
                .viewCount(0L)
                .build();

        Chapter savedChapter = chapterRepository.save(chapter);
        return chapterMapper.chapterToChapterResponse(savedChapter);
    }

    @Override
    public ChapterResponse updateChapter(Long novelId, Long chapterId, ChapterRequest chapterRequest) {
        Chapter chapter = chapterRepository.findByIdAndNovelId(chapterId, novelId)
                .orElseThrow(() -> new EntityNotFoundException("Chapter not found with id " + chapterId + " and novelId " + novelId));

        chapter.setTitle(chapterRequest.getTitle());
        chapter.setContent(chapterRequest.getContent());
        chapter.setChapterNumber(chapterRequest.getChapterNumber());

        Chapter updatedChapter = chapterRepository.save(chapter);
        return chapterMapper.chapterToChapterResponse(updatedChapter);
    }

    @Override
    public void deleteChapter(Long novelId, Long chapterId) {
        Chapter chapter = chapterRepository.findByIdAndNovelId(chapterId, novelId)
                .orElseThrow(() -> new EntityNotFoundException("Chapter not found with id " + chapterId + " and novelId " + novelId));
        chapterRepository.delete(chapter);
    }

    @Override
    public void incrementViewCount(Long novelId, Long chapterId) {
        Chapter chapter = chapterRepository.findByIdAndNovelId(chapterId, novelId)
                .orElseThrow(() -> new EntityNotFoundException("Chapter not found with id " + chapterId + " and novelId " + novelId));
        chapter.setViewCount(chapter.getViewCount() + 1);
        chapterRepository.save(chapter);
    }
}