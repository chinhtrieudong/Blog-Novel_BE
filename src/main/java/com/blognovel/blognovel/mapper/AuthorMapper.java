package com.blognovel.blognovel.mapper;

import com.blognovel.blognovel.dto.response.AuthorResponse;
import com.blognovel.blognovel.model.Author;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract class AuthorMapper {
    public abstract AuthorResponse toResponse(Author author);
}
