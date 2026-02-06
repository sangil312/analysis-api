package com.dev.analysis.support.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;


@Getter
@RequiredArgsConstructor
public class ApiException extends RuntimeException {
    private final ErrorType errorType;
}
