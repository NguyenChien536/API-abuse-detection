package com.apiabusedetection.user.service;


import com.apiabusedetection.common.exception.AppException;
import com.apiabusedetection.common.exception.ErrorCode;
import com.apiabusedetection.user.dto.request.UserCreationRequest;
import com.apiabusedetection.user.dto.request.UserUpdateRequest;
import com.apiabusedetection.user.dto.response.UserResponse;
import com.apiabusedetection.user.entity.Role;
import com.apiabusedetection.user.entity.User;
import com.apiabusedetection.user.mapper.UserMapper;
import com.apiabusedetection.user.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserService {
    UserRepository userRepository;
    UserMapper userMapper;
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

        HashSet<String> roles = new HashSet<>();
        roles.add(Role.USER.name());

        user.setRoles(roles);
        user.setEnabled(true);

        return userMapper.toUserResponse(userRepository.save(user));
    }

    @Transactional(readOnly = true)
    public List<UserResponse> getUsers() {
        return userRepository.findAll().stream()
                .map(userMapper::toUserResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public UserResponse getUser(Long id) {
        return userMapper.toUserResponse(userRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND)));
    }

    @Transactional
    public UserResponse updateUser(Long id, UserUpdateRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        if (userRepository.existsByUsernameAndIdNot(request.getUsername(), id)) {
            throw new AppException(ErrorCode.USER_EXISTS);
        }
        if (userRepository.existsByEmailAndIdNot(request.getEmail(), id)) {
            throw new AppException(ErrorCode.EMAIL_EXISTS);
        }

        userMapper.updateUser(user, request);
        user.setPasswordHash(passwordEncoder.encode(user.getPasswordHash()));
        if (request.getEnabled() != null) {
            user.setEnabled(request.getEnabled());
        }
        return userMapper.toUserResponse(userRepository.save(user));
    }

    @Transactional
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new AppException(ErrorCode.USER_NOT_FOUND);
        }
        userRepository.deleteById(id);
    }
}
