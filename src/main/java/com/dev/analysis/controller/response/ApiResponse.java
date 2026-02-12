package com.dev.analysis.controller.response;

import com.dev.analysis.support.error.ErrorResponse;
import com.dev.analysis.support.error.ErrorType;

public record ApiResponse<T>(
        ResultType resultType,
        T data,
        ErrorResponse error
) {
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(ResultType.SUCCESS, data, null);
    }

    public static <T> ApiResponse<T> error(ErrorType errorType) {
        return new ApiResponse<>(ResultType.ERROR, null, ErrorResponse.of(errorType));
    }
}
