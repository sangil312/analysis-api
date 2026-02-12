package com.dev.analysis.api.controller;

import com.dev.analysis.api.controller.response.Response;
import com.dev.analysis.support.error.ApiException;
import com.dev.analysis.support.error.ErrorType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Response<Object>> handleException(Exception e) {
        log.error("Exception: {}", e.getMessage(), e);
        return ResponseEntity.internalServerError().body(Response.error(ErrorType.SERVER_ERROR));
    }

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<Response<Object>> handleApiException(ApiException e) {
        ErrorType errorType = e.getErrorType();

        switch (errorType.getLogLevel()) {
            case ERROR -> log.error("ApiException: {}", e.getMessage(), e);
            case WARN -> log.warn("ApiException: {}", e.getMessage(), e);
            default -> log.info("ApiException: {}", e.getMessage(), e);
        }
        return ResponseEntity.status(errorType.getHttpStatus()).body(Response.error(errorType));
    }
}
