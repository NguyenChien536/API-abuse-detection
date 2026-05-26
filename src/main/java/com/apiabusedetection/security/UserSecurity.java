package com.apiabusedetection.security;

import com.apiabusedetection.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component("userSecurity")
@RequiredArgsConstructor
public class UserSecurity {
    private final UserRepository userRepository;

    public boolean isOwner(Long id) {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }

        return userRepository.findById(id)
                .map(user -> user.getUsername().equals(authentication.getName()))
                .orElse(false);
    }
}
