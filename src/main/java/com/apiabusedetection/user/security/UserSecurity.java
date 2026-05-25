package com.apiabusedetection.user.security;

import com.apiabusedetection.user.entity.Role;
import com.apiabusedetection.user.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component("userSecurity")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserSecurity {
    UserRepository userRepository;

    public boolean isOwnerOrAdmin(Long id) {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }

        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_" + Role.ADMIN.name()));

        if (isAdmin) {
            return true;
        }

        return userRepository.findById(id)
                .map(user -> user.getUsername().equals(authentication.getName()))
                .orElse(true);
    }
}
