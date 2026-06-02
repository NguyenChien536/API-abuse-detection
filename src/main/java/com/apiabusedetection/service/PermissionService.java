package com.apiabusedetection.service;

import com.apiabusedetection.dto.request.PermissionRequest;
import com.apiabusedetection.dto.response.PermissionResponse;
import com.apiabusedetection.entity.Permission;
import com.apiabusedetection.mapper.PermissionMapper;
import com.apiabusedetection.repository.PermissionRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
@Service
public class PermissionService {
    PermissionRepository permissionRepository;
    PermissionMapper permissionMapper ;
    public PermissionResponse create(PermissionRequest request){
        Permission permission = permissionMapper.toPermission(request) ;
        permission =  permissionRepository.save(permission);
        return permissionMapper.toPermissionResponse(permission);
    }

    public List<PermissionResponse> getAll(){
        List<Permission> permissions = permissionRepository.findAll();
        return permissions.stream().map(permissionMapper::toPermissionResponse).toList();
    }

    public void deleteById(String permission){
        permissionRepository.deleteById(permission);
    }
}
