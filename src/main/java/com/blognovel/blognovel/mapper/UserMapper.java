package com.blognovel.blognovel.mapper;

import com.blognovel.blognovel.dto.request.UserRequest;
import com.blognovel.blognovel.dto.response.UserResponse;
import com.blognovel.blognovel.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(source = "createdAt", target = "createdAt", dateFormat = "yyyy-MM-dd HH:mm:ss")
    UserResponse toResponse(User user);

    User toEntity(UserRequest request);
}

