package com.apiabusedetection.controller;

import com.apiabusedetection.common.response.ApiResponse;
import com.apiabusedetection.dto.request.PermissionRequest;
import com.apiabusedetection.dto.request.RoleRequest;
import com.apiabusedetection.dto.response.PermissionResponse;
import com.apiabusedetection.dto.response.RoleReponse;
import com.apiabusedetection.service.PermissionService;
import com.apiabusedetection.service.RoleService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/permission")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoleController {
    RoleService roleService;

    @PostMapping
    ApiResponse<RoleReponse> create(@RequestBody RoleRequest request){
        return ApiResponse.<RoleReponse>builder()
                .result(roleService.create(request))
                .build();
    }

    @GetMapping
    ApiResponse<List<RoleReponse>> getAll(){
        return ApiResponse.<List<RoleReponse>>builder()
                .result(roleService.getRoles())
                .build();
    }

    @DeleteMapping("/{permission}")
    ApiResponse<Void> delete(@PathVariable String permission){
        roleService.deleteRole(permission);
        return ApiResponse.<Void>builder().build();
    }
}
