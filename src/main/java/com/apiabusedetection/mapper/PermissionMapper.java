package com.apiabusedetection.mapper;

import com.apiabusedetection.dto.request.PermissionRequest;
import com.apiabusedetection.dto.request.UserCreationRequest;
import com.apiabusedetection.dto.request.UserUpdateRequest;
import com.apiabusedetection.dto.response.PermissionResponse;
import com.apiabusedetection.dto.response.UserResponse;
import com.apiabusedetection.entity.Permission;
import com.apiabusedetection.entity.Role;
import com.apiabusedetection.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface PermissionMapper {

    Permission toPermission(PermissionRequest request);

    PermissionResponse toPermissionResponse(Permission permission);
}
