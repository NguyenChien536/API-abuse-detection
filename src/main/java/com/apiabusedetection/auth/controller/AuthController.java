package com.apiabusedetection.auth.controller;

import com.apiabusedetection.auth.dto.request.AuthRequest;
import com.apiabusedetection.auth.dto.request.IntrospectRequest;
import com.apiabusedetection.auth.dto.response.AuthResponse;
import com.apiabusedetection.auth.dto.response.IntrospectResponse;
import com.apiabusedetection.auth.service.AuthService;
import com.apiabusedetection.common.response.ApiResponse;
import com.nimbusds.jose.JOSEException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AuthController {
    AuthService authService;
    @PostMapping("/token")
    ApiResponse<AuthResponse> authenticate(@RequestBody AuthRequest request){
        var result = authService.authenticate(request);
        return ApiResponse.<AuthResponse>builder()
                .result(result)
                .build();
    }
    @PostMapping("/introspect")
    ApiResponse<IntrospectResponse> introspect (@RequestBody IntrospectRequest request) throws ParseException, JOSEException {
        var result = authService.introspect(request);
        return ApiResponse.<IntrospectResponse>builder()
                .result(result)
                .build();
    }

}
