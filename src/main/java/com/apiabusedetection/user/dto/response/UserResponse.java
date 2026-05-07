package com.apiabusedetection.user.dto.response;

import com.apiabusedetection.user.entity.Role;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserResponse {
    Long id;
    String username;
    String email;
    String passwordHash;
    Role role;
    @Builder.Default
    boolean enabled = true;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}
