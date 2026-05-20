package com.apiabusedetection.common.exception;

import com.apiabusedetection.common.response.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    ResponseEntity<ApiResponse> handlingException(Exception exception) {
        log.error("Unhandled exception", exception);
        return ResponseEntity.internalServerError().body(buildResponse(ErrorCode.UNCATEGORIZED_EXCEPTION));
    }

    @ExceptionHandler(value = AppException.class)
    ResponseEntity<ApiResponse> handlingAppException(AppException exception) {
        return ResponseEntity.badRequest().body(buildResponse(exception.getErrorCode()));
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    ResponseEntity<ApiResponse> handlingException(MethodArgumentNotValidException exception) {
        String enumKey = exception.getFieldError().getDefaultMessage();
        ErrorCode errorCode = ErrorCode.INVALID_KEY;

        try {
            errorCode = ErrorCode.valueOf(enumKey);
        } catch (IllegalArgumentException ignored) {
        }

        return ResponseEntity.badRequest().body(buildResponse(errorCode));
    }

    @ExceptionHandler(value = DataIntegrityViolationException.class)
    ResponseEntity<ApiResponse> handlingDataIntegrityViolationException(DataIntegrityViolationException exception) {
        log.warn("Database constraint violation", exception);

        String message = exception.getMostSpecificCause() != null
                ? exception.getMostSpecificCause().getMessage()
                : "";
        String normalizedMessage = message.toLowerCase();

        ErrorCode errorCode = ErrorCode.UNCATEGORIZED_EXCEPTION;
        HttpStatus status = HttpStatus.BAD_REQUEST;

        if (normalizedMessage.contains("email")) {
            errorCode = normalizedMessage.contains("cannot be null")
                    ? ErrorCode.BLANK_EMAIL
                    : ErrorCode.EMAIL_EXISTS;
            status = normalizedMessage.contains("cannot be null")
                    ? HttpStatus.BAD_REQUEST
                    : HttpStatus.CONFLICT;
        } else if (normalizedMessage.contains("username")) {
            errorCode = normalizedMessage.contains("cannot be null")
                    ? ErrorCode.USERNAME_REQUIRED
                    : ErrorCode.USER_EXISTS;
            status = normalizedMessage.contains("cannot be null")
                    ? HttpStatus.BAD_REQUEST
                    : HttpStatus.CONFLICT;
        }

        return ResponseEntity.status(status).body(buildResponse(errorCode));
    }

    private ApiResponse buildResponse(ErrorCode errorCode) {
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setCode(errorCode.getCode());
        apiResponse.setMessage(errorCode.getMessage());
        return apiResponse;
    }
}
