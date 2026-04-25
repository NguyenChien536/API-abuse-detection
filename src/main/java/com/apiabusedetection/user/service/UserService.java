package com.apiabusedetection.user.service;


import com.apiabusedetection.user.dto.request.UserCreationRequest;
import com.apiabusedetection.user.dto.request.UserUpdateRequest;
import com.apiabusedetection.user.entity.User;
import com.apiabusedetection.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public User createUser(UserCreationRequest request) {
        User user = new User();

        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username is already in use");
        }

        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPasswordHash(request.getPassword());
        user.setRole(request.getRole());
        user.setEnabled(request.isEnabled());

        return userRepository.save(user);
    }

    public List<User> getUsers(){
        return userRepository.findAll();
    }
    public User getUser(Long id){
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
    public User updateUser(Long id,UserUpdateRequest request) {
        User user = getUser(id);

        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPasswordHash(request.getPassword());
        user.setRole(request.getRole());
        user.setEnabled(request.isEnabled());

        return userRepository.save(user);
    }
    public void deleteUser(Long id){
        userRepository.deleteById(id);
     }
}
