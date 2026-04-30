package com.apiabusedetection.exception;

public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(9999, "Uncategorized error"),
    USER_EXISTS(1001, "User already exists"),
    USER_NOT_FOUND(1002, "User not found"),
    USERNAME_INVALID(1003, "Username must be at least 3 characters, username may only contain letters, numbers, dot, underscore, and hyphen"),
    INVALID_PASSWORD(1004, "Password must be between 8 and 72 characters"),
    INVALID_KEY(1005, "Invalid message KEY"),
    INVALID_EMAIL(1006, "Invalid email"),
    BLANK_EMAIL(1007, "Email is required"),
    ROLE_REQUIRED(1008, "Role is required"),
    USERNAME_REQUIRED(1009, "Username is required"),
    PASSWORD_REQUIRED(1010, "Password is required");

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

