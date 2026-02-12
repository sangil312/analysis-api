package com.dev.analysis.application.service.accesslog.parser;

import com.dev.analysis.application.service.accesslog.AnalysisValidator;
import com.dev.analysis.application.service.accesslog.dto.AccessLogParseResult;
import com.opencsv.bean.exceptionhandler.CsvExceptionHandler;
import com.opencsv.exceptions.CsvException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

@RequiredArgsConstructor
@Slf4j
public class AccessLogCsvExceptionHandler implements CsvExceptionHandler {
    private final AnalysisValidator analysisValidator;
    private final AccessLogParseResult accessLogParseResult;

    /**
     * CSV 파일 파싱 실패 row 처리
     */
    @Override
    public CsvException handleException(CsvException e) {
        String reason = StringUtils.hasText(e.getMessage()) ? e.getMessage() : "CSV 파싱 오류";

        accessLogParseResult.addParseError(e.getLineNumber(), reason);

        analysisValidator.validateFileLine(accessLogParseResult.getTotalRequestCount());

        log.debug("csv 파싱 실패: lineNo: {}, reason: {}", e.getLineNumber(), reason);

        return null;
    }
}
