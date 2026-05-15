package com.apiabusedetection.auth.controller;

import com.apiabusedetection.auth.dto.AuthRequest;
import com.apiabusedetection.auth.dto.AuthResponse;
import com.apiabusedetection.auth.service.AuthService;
import com.apiabusedetection.common.response.ApiResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AuthController {
    AuthService authService;
    @PostMapping("/log-in")
    ApiResponse<AuthResponse> authenticate(@RequestBody AuthRequest request){
        boolean result = authService.authenticate(request);
        return ApiResponse.<AuthResponse>builder()
                .result(AuthResponse.builder()
                        .authenticated(result)
                        .build())
                .build();
    }
}
