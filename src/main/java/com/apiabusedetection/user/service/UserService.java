package com.apiabusedetection.user.service;


import com.apiabusedetection.common.exception.AppException;
import com.apiabusedetection.common.exception.ErrorCode;
import com.apiabusedetection.user.dto.request.UserCreationRequest;
import com.apiabusedetection.user.dto.request.UserUpdateRequest;
import com.apiabusedetection.user.dto.response.UserResponse;
import com.apiabusedetection.user.entity.User;
import com.apiabusedetection.user.mapper.UserMapper;
import com.apiabusedetection.user.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserService {
     UserRepository userRepository;
     UserMapper userMapper;

    public User createUser(UserCreationRequest request) {

        if (userRepository.existsByUsername(request.getUsername())) {
            throw new AppException(ErrorCode.USER_EXISTS);
        }
        User user = userMapper.toUser(request);

        return userRepository.save(user);
    }

    public List<User> getUsers(){
        return userRepository.findAll();
    }

    public UserResponse getUser(Long id){
        return userMapper.toUserResponse(userRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND)));
    }
    public UserResponse updateUser(Long id,UserUpdateRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        userMapper.updateUser(user, request);
        return userMapper.toUserResponse(userRepository.save(user));
    }
    public void deleteUser(Long id){
        userRepository.deleteById(id);
     }
}
