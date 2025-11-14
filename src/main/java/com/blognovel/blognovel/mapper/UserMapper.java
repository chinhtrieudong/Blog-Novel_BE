package com.blognovel.blognovel.mapper;

import com.blognovel.blognovel.dto.request.UserRequest;
import com.blognovel.blognovel.dto.response.UserResponse;
import com.blognovel.blognovel.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

    @Mapping(source = "role", target = "role")
    @Mapping(source = "status", target = "status")
    UserResponse toResponse(User user);

    User toEntity(UserRequest request);
}
