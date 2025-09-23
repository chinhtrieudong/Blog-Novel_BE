package com.blognovel.blognovel.mapper;

import com.blognovel.blognovel.dto.response.ChapterResponse;
import com.blognovel.blognovel.model.Chapter;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ChapterMapper {

    ChapterResponse chapterToChapterResponse(Chapter chapter);
}