package com.apiabusedetection.common.exception;

public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(9999, "Uncategorized error"),
    USER_EXISTS(1001, "User already exists"),
    USER_NOT_EXISTS(1002, "User not exists"),
    USER_NOT_FOUND(1003, "User not found"),
    USERNAME_INVALID(1004, "Username must be at least 3 characters, username may only contain letters, numbers, dot, underscore, and hyphen"),
    INVALID_PASSWORD(1005, "Password must be between 8 and 72 characters"),
    INVALID_KEY(1006, "Invalid message KEY"),
    INVALID_EMAIL(1007, "Invalid email"),
    BLANK_EMAIL(1008, "Email is required"),
    ROLE_REQUIRED(1009, "Role is required"),
    USERNAME_REQUIRED(1010, "Username is required"),
    PASSWORD_REQUIRED(1011, "Password is required");

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    private int code;
    private String message;

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}

