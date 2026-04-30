package com.apiabusedetection.user.dto.request;

import com.apiabusedetection.user.entity.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserCreationRequest {
    @NotBlank(message = "USERNAME_REQUIRED")
    @Size(min = 3, max = 50, message = "USERNAME_INVALID")
    @Pattern(
            regexp = "^[a-zA-Z0-9._-]+$",
            message = "USERNAME_INVALID"
    )
    String username;

    @NotBlank(message = "BLANK_EMAIL")
    @Email(message = "INVALID_EMAIL")
    @Size(max = 100, message = "INVALID_EMAIL")
    String email;

    @NotBlank(message = "PASSWORD_REQUIRED")
    @Size(min = 8, max = 72, message = "INVALID_PASSWORD")
    String password;

    @NotNull(message = "ROLE_REQUIRED")
    Role role;

    @Builder.Default
    boolean enabled = true;
}
