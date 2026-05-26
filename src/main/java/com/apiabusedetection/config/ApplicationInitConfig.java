package com.apiabusedetection.config;

import com.apiabusedetection.entity.Role;
import com.apiabusedetection.entity.User;
import com.apiabusedetection.entity.Permission;
import com.apiabusedetection.repository.PermissionRepository;
import com.apiabusedetection.repository.RoleRepository;
import com.apiabusedetection.repository.UserRepository;
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
import java.util.Set;

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

    // chạy mỗi khi đc sạc lên
    @Bean
    @ConditionalOnProperty(prefix = "app.bootstrap-admin", name = "enabled", havingValue = "true")
    ApplicationRunner applicationRunner(
            UserRepository userRepository,
            RoleRepository roleRepository,
            PermissionRepository permissionRepository
    ) {
        return args -> {
            Permission userRead = ensurePermission(permissionRepository, "user:read", "Read any user");
            Permission userList = ensurePermission(permissionRepository, "user:list", "List all users");
            Permission userUpdate = ensurePermission(permissionRepository, "user:update", "Update any user");
            Permission userDelete = ensurePermission(permissionRepository, "user:delete", "Delete any user");

            ensureRole(roleRepository, "USER", "Default application user", Set.of());
            Role adminRole = ensureRole(
                    roleRepository,
                    "ADMIN",
                    "System administrator",
                    Set.of(userRead, userList, userUpdate, userDelete)
            );

            if (userRepository.findByUsername(adminUsername).isPresent()) {
                return;
            }

            var roles = new HashSet<Role>();
            roles.add(adminRole);

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

    private Permission ensurePermission(PermissionRepository permissionRepository, String name, String description) {
        return permissionRepository.findById(name)
                .map(existingPermission -> {
                    existingPermission.setDescription(description);
                    return permissionRepository.save(existingPermission);
                })
                .orElseGet(() -> permissionRepository.save(Permission.builder()
                        .name(name)
                        .description(description)
                        .build()));
    }

    private Role ensureRole(RoleRepository roleRepository, String name, String description, Set<Permission> permissions) {
        return roleRepository.findById(name)
                .map(existingRole -> {
                    existingRole.setDescription(description);
                    existingRole.setPermissions(new HashSet<>(permissions));
                    return roleRepository.save(existingRole);
                })
                .orElseGet(() -> roleRepository.save(Role.builder()
                        .name(name)
                        .description(description)
                        .permissions(new HashSet<>(permissions))
                        .build()));
    }
}
