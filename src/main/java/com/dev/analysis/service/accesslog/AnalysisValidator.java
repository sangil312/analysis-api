package com.dev.analysis.service.accesslog;

import com.dev.analysis.support.error.ApiException;
import com.dev.analysis.support.error.ErrorType;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;

@Component
public class AnalysisValidator {
    private static final long MAX_FILE_SIZE_BYTES = 50L * 1024 * 1024;
    private static final long MAX_FILE_LINE = 200000L;

    public void validateFile(MultipartFile file) {
        if (Objects.isNull(file) || file.isEmpty()) {
            throw new ApiException(ErrorType.EMPTY_FILE);
        }
        if (file.getSize() > MAX_FILE_SIZE_BYTES) {
            throw new ApiException(ErrorType.MAX_FILE_SIZE_OVER);
        }
    }

    public void validateFileLine(Long lineCount) {
        if (lineCount > MAX_FILE_LINE) {
            throw new ApiException(ErrorType.MAX_FILE_LINE_OVER);
        }
    }
}
