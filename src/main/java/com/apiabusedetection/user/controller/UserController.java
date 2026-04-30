package com.apiabusedetection.user.controller;

import com.apiabusedetection.user.dto.request.UserCreationRequest;
import com.apiabusedetection.user.dto.request.UserUpdateRequest;
import com.apiabusedetection.user.dto.response.ApiResponse;
import com.apiabusedetection.user.entity.User;
import com.apiabusedetection.user.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping
    ApiResponse createUser(@RequestBody @Valid UserCreationRequest request) {
        ApiResponse<User> apiResponse = new ApiResponse<>();
        apiResponse.setResult(userService.createUser(request));
        return apiResponse;
    }

    @GetMapping
    List<User> getUser(){
        userService.getUsers();
        return userService.getUsers();
    }

    @GetMapping("/{userId}")
    User getUser(@PathVariable Long userId){
        return userService.getUser(userId);
    }

    @PutMapping("/{userId}")
    User updateUser(@PathVariable Long userId, @RequestBody @Valid UserUpdateRequest request){
        return userService.updateUser(userId, request);
    }

    @DeleteMapping("/{userId}")
    String deleteUser(@PathVariable Long userId){
        userService.deleteUser(userId);
        return "User has been deleted";
    }
}
