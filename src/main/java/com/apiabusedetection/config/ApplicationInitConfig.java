package com.apiabusedetection.config;

import com.apiabusedetection.user.entity.Role;
import com.apiabusedetection.user.entity.User;
import com.apiabusedetection.user.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;

@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Slf4j
public class ApplicationInitConfig {

    final PasswordEncoder passwordEncoder;

    @Value("${app.bootstrap-admin.username:admin}")
    String adminUsername;

    @Value("${app.bootstrap-admin.password:admin}")
    String adminPassword;

    @Value("${app.bootstrap-admin.email:admin@example.com}")
    String adminEmail;

    @Bean
    @ConditionalOnProperty(prefix = "app.bootstrap-admin", name = "enabled", havingValue = "true")
    ApplicationRunner applicationRunner(UserRepository userRepository) {
        return args -> {
            if (userRepository.findByUsername(adminUsername).isPresent()) {
                return;
            }

            var roles = new HashSet<String>();
            roles.add(Role.ADMIN.name());

            User user = User.builder()
                    .username(adminUsername)
                    .email(adminEmail)
                    .passwordHash(passwordEncoder.encode(adminPassword))
                    .roles(roles)
                    .enabled(true)
                    .build();

            userRepository.save(user);
            log.warn("Bootstrap admin created for username '{}'. Rotate the password immediately.", adminUsername);
        };
    }
}
