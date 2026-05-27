package com.apiabusedetection.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserResponse {
    Long id;
    String username;
    String email;
    Set<RoleReponse> roles;
    @Builder.Default
    boolean enabled = true;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}
