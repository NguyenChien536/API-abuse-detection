package com.apiabusedetection.mapper;


import com.apiabusedetection.dto.request.RoleRequest;
import com.apiabusedetection.dto.response.RoleReponse;
import com.apiabusedetection.entity.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    @Mapping(target = "permissions", ignore = true)
    Role toRole(RoleRequest request);

    RoleReponse toRoleResponse(Role role);
}
