package com.apiabusedetection.mapper;

import com.apiabusedetection.dto.request.UserCreationRequest;
import com.apiabusedetection.dto.request.UserUpdateRequest;
import com.apiabusedetection.dto.response.UserResponse;
import com.apiabusedetection.entity.Role;
import com.apiabusedetection.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "passwordHash", source = "password")
    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "enabled", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    User toUser(UserCreationRequest request);

    UserResponse toUserResponse(User user);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "passwordHash", source = "password")
    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "enabled", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateUser(@MappingTarget User user, UserUpdateRequest request);

    default String map(Role role) {
        return role == null ? null : role.getName();
    }
}
