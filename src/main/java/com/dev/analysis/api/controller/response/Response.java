package com.dev.analysis.api.controller.response;

import com.dev.analysis.support.error.ErrorResponse;
import com.dev.analysis.support.error.ErrorType;
import io.swagger.v3.oas.annotations.media.Schema;

public record Response<T>(
        ResultType resultType,
        @Schema(nullable = true, example = "null")
        T data,
        @Schema(nullable = true, example = "null")
        ErrorResponse error
) {
    public static <T> Response<T> success(T data) {
        return new Response<>(ResultType.SUCCESS, data, null);
    }

    public static <T> Response<T> error(ErrorType errorType) {
        return new Response<>(ResultType.ERROR, null, ErrorResponse.of(errorType));
    }
}
