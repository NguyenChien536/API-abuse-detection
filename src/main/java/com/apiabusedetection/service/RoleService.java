package com.apiabusedetection.service;

import com.apiabusedetection.dto.request.RoleRequest;
import com.apiabusedetection.dto.response.RoleReponse;
import com.apiabusedetection.mapper.RoleMapper;
import com.apiabusedetection.repository.PermissionRepository;
import com.apiabusedetection.repository.RoleRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class RoleService {
    RoleRepository rolerepository;
    PermissionRepository permissionrepository;
    RoleMapper rolemapper;
    public RoleReponse create(RoleRequest request){
        var role = rolemapper.toRole(request);

        var permissions = permissionrepository.findAllById(request.getPermissions());
        role.setPermissions(new HashSet<>(permissions));

        role = rolerepository.save(role);
       return rolemapper.roRoleResponse(role);
    }
    public List<RoleReponse> getRoles(){
        return rolerepository.findAll()
                .stream().map(rolemapper::roRoleResponse)
                .toList();
    }
    public void deleteRole(String role){
        rolerepository.deleteById(role);
    }
}
