package com.dev.analysis.support.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.logging.LogLevel;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorType {
    // 공통
    SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, LogLevel.ERROR, "알 수 없는 오류가 발생했습니다. 잠시 후 다시 시도해주세요."),
    INVALID_REQUEST(HttpStatus.BAD_REQUEST, LogLevel.INFO, "요청이 올바르지 않습니다."),

    // 파일
    EMPTY_FILE(HttpStatus.BAD_REQUEST, LogLevel.INFO, "빈 파일은 업로드할 수 없습니다."),
    MAX_FILE_SIZE_OVER(HttpStatus.CONTENT_TOO_LARGE, LogLevel.INFO, "파일 크기 제한을 초과했습니다."),
    MAX_FILE_LINE_OVER(HttpStatus.CONTENT_TOO_LARGE, LogLevel.INFO, "파일 라인 수 제한을 초과했습니다."),
    INVALID_DATA(HttpStatus.BAD_REQUEST, LogLevel.INFO, "파일 내 데이터가 올바르지 않습니다."),
    ;

    private final HttpStatus httpStatus;
    private final LogLevel logLevel;
    private final String message;
}
