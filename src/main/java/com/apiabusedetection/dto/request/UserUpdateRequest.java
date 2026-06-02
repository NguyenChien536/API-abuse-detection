package com.apiabusedetection.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserUpdateRequest {

    @Size(min = 3, max = 50, message = "USERNAME_INVALID")
    @Pattern(
            regexp = "^[a-zA-Z0-9._-]+$",
            message = "USERNAME_INVALID"
    )
    String username;


    @Email(message = "INVALID_EMAIL")
    @Size(max = 100, message = "INVALID_EMAIL")
    String email;


    @Size(min = 8, max = 72, message = "INVALID_PASSWORD")
    String password;

    List<String> roles;

    Boolean enabled;
}
