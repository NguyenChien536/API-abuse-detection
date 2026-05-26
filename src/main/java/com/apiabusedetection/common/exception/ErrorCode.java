package com.apiabusedetection.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
@Getter
public enum ErrorCode {

    UNCATEGORIZED_EXCEPTION(9999, "Uncategorized error", HttpStatus.INTERNAL_SERVER_ERROR),
    USER_EXISTS(1001, "User already exists",HttpStatus.BAD_REQUEST),
    USER_NOT_EXISTS(1002, "User not exists",HttpStatus.NOT_FOUND),
    USER_NOT_FOUND(1003, "User not found",HttpStatus.NOT_FOUND),
    USERNAME_INVALID(1004, "Username must be at least 3 characters, username may only contain letters, numbers, dot, underscore, and hyphen",HttpStatus.BAD_REQUEST),
    INVALID_PASSWORD(1005, "Password must be between 8 and 72 characters",HttpStatus.BAD_REQUEST),
    INVALID_KEY(1006, "Invalid message KEY",HttpStatus.BAD_REQUEST),
    INVALID_EMAIL(1007, "Invalid email",HttpStatus.BAD_REQUEST),
    BLANK_EMAIL(1008, "Email is required",HttpStatus.BAD_REQUEST),
    ROLE_REQUIRED(1009, "Role is required",HttpStatus.BAD_REQUEST),
    USERNAME_REQUIRED(1010, "Username is required",HttpStatus.BAD_REQUEST),
    PASSWORD_REQUIRED(1011, "Password is required",HttpStatus.BAD_REQUEST),
    UNAUTHENTICATED(1012, "Unauthenticated",HttpStatus.UNAUTHORIZED),
    EMAIL_EXISTS(1013, "Email already exists",HttpStatus.BAD_REQUEST),
    UNAUTHORIZED(1014, "You do not have permission",HttpStatus.FORBIDDEN),
    USER_DISABLED(1015, "User account is disabled", HttpStatus.FORBIDDEN),;

    ErrorCode(int code, String message, HttpStatusCode httpStatusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = httpStatusCode;
    }

    private int code;
    private String message;
    private HttpStatusCode statusCode;

}

