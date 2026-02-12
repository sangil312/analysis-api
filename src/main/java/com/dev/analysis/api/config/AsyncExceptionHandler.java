package com.dev.analysis.api.config;

import com.dev.analysis.support.error.ApiException;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.Nullable;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;

import java.lang.reflect.Method;

@Slf4j
public class AsyncExceptionHandler implements AsyncUncaughtExceptionHandler {

    @Override
    public void handleUncaughtException(Throwable throwable, Method method, @Nullable Object... params) {
        if (throwable instanceof ApiException e) {
            switch (e.getErrorType().getLogLevel()) {
                case ERROR -> log.error("ApiException: {}", e.getMessage(), e);
                case WARN -> log.warn("ApiException: {}", e.getMessage(), e);
                default -> log.info("ApiException: {}", e.getMessage(), e);
            }
        } else {
            log.error("[Async] Exception: {}", throwable.getMessage(), throwable);
        }
    }
}
