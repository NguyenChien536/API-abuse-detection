package com.apiabusedetection.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RoleReponse {
    String role;
    String description;
    Set<PermissionResponse> permissions;
}
