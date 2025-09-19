package com.blognovel.blognovel.mapper;

import com.blognovel.blognovel.dto.response.ChapterResponse;
import com.blognovel.blognovel.model.Chapter;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ChapterMapper {

    ChapterMapper INSTANCE = Mappers.getMapper(ChapterMapper.class);

    ChapterResponse chapterToChapterResponse(Chapter chapter);
}