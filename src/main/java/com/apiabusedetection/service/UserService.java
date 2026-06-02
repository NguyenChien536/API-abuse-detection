package com.apiabusedetection.service;


import com.apiabusedetection.common.exception.AppException;
import com.apiabusedetection.common.exception.ErrorCode;
import com.apiabusedetection.dto.request.UserCreationRequest;
import com.apiabusedetection.dto.request.UserUpdateRequest;
import com.apiabusedetection.dto.response.UserResponse;
import com.apiabusedetection.entity.User;
import com.apiabusedetection.mapper.UserMapper;
import com.apiabusedetection.repository.RoleRepository;
import com.apiabusedetection.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserService {
    UserRepository userRepository;
    UserMapper userMapper;
    RoleRepository roleRepository;
    PasswordEncoder passwordEncoder;

    @Transactional
    public UserResponse createUser(UserCreationRequest request) {
        if (request.getUsername() == null || request.getUsername().isBlank()) {
            throw new AppException(ErrorCode.USERNAME_REQUIRED);
        }
        if (request.getEmail() == null || request.getEmail().isBlank()) {
            throw new AppException(ErrorCode.BLANK_EMAIL);
        }

        if (userRepository.existsByUsername(request.getUsername())) {
            throw new AppException(ErrorCode.USER_EXISTS);
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new AppException(ErrorCode.EMAIL_EXISTS);
        }
        User user = userMapper.toUser(request);

        user.setPasswordHash(passwordEncoder.encode(user.getPasswordHash()));

        var userRole = roleRepository.findById("USER")
                .orElseGet(() -> roleRepository.save(
                        com.apiabusedetection.entity.Role.builder()
                                .name("USER")
                                .description("Default application user")
                                .permissions(new HashSet<>())
                                .build()
                ));

        HashSet<com.apiabusedetection.entity.Role> roles = new HashSet<>();
        roles.add(userRole);

        user.setRoles(roles);
        user.setEnabled(true);

        return userMapper.toUserResponse(userRepository.save(user));
    }
    @PreAuthorize("hasAuthority('user:list')")
    @Transactional(readOnly = true)
    public List<UserResponse> getUsers() {
        log.info("In method getUsers");
        return userRepository.findAll().stream()
                .map(userMapper::toUserResponse)
                .toList();
    }

    @PostAuthorize("hasAuthority('user:read') or returnObject.username == authentication.name")
    @Transactional(readOnly = true)
    public UserResponse getUserById(Long id) {
        log.info("In method get user by id");
        User user = userRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        return userMapper.toUserResponse(user);
    }


    public UserResponse getMyInfo() {
        var context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();

        User user = userRepository.findByUsername(name).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTS));
        return userMapper.toUserResponse(user);
    }

    @PreAuthorize("hasAuthority('user:update') or @userSecurity.isOwner(#id)")
    @Transactional
    public UserResponse updateUser(Long id, UserUpdateRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        if (request.getUsername() != null && userRepository.existsByUsernameAndIdNot(request.getUsername(), id)) {
            throw new AppException(ErrorCode.USER_EXISTS);
        }
        if (request.getEmail() != null && userRepository.existsByEmailAndIdNot(request.getEmail(), id)) {
            throw new AppException(ErrorCode.EMAIL_EXISTS);
        }

        userMapper.updateUser(user, request);

        if (request.getPassword() != null) {
            user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        }

        if (request.getRoles() != null) {
            var roles = roleRepository.findAllById(request.getRoles());
            user.setRoles(new HashSet<>(roles));
        }

        return userMapper.toUserResponse(userRepository.save(user));
    }

    @PreAuthorize("hasAuthority('user:delete')")
    @Transactional
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new AppException(ErrorCode.USER_NOT_FOUND);
        }
        userRepository.deleteById(id);
    }
}
