package com.dev.analysis.parser;

import com.dev.analysis.service.accesslog.AnalysisValidator;
import com.dev.analysis.support.error.ApiException;
import com.dev.analysis.support.error.ErrorType;
import com.opencsv.bean.CsvToBean;
import com.opencsv.exceptions.CsvException;
import com.dev.analysis.service.accesslog.dto.AccessLogParseResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

/**
 * 접속 로그 csv 파일 parser
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class AccessLogParser {
    private final CsvFactory csvFactory;
    private final AnalysisValidator analysisValidator;

    public AccessLogParseResult parse(MultipartFile file) {
        log.info("csv 분석 시작: 파일명: {}, 파일 사이즈: {}byte", file.getOriginalFilename(), file.getSize());

        AccessLogParseResult parseResult = new AccessLogParseResult(5);

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))
        ) {
            CsvToBean<AccessLogCsvRow> csvRows = csvFactory.create(reader, AccessLogCsvRow.class);

            long lineCount = 0L;
            for (AccessLogCsvRow row : csvRows) {
                analysisValidator.validateFileLine(++lineCount);
                parseResult.addParseResult(row.getClientIp(), row.getRequestUri(), row.getHttpStatus());
            }

            if (!parseResult.checkResult()) {
                throw new ApiException(ErrorType.INVALID_DATA);
            }

            addParseError(lineCount, parseResult, csvRows);

            log.info("csv 분석 종료: 파일명: {}, 파일 사이즈: {}byte, 파싱 실패 line 수: {}",
                    file.getOriginalFilename(), file.getSize(), csvRows.getCapturedExceptions().size());

            return parseResult;
        } catch (IOException e) {
            log.error("csv 파일 읽기 중 오류 발생 Exception: {}", e.getMessage(), e);
            throw new ApiException(ErrorType.SERVER_ERROR);
        }
    }

    private void addParseError(
            long lineCount,
            AccessLogParseResult parseResult,
            CsvToBean<AccessLogCsvRow> csvRows
    ) {
        for (CsvException exception : csvRows.getCapturedExceptions()) {
            analysisValidator.validateFileLine(++lineCount);
            String reason = extractReason(exception);
            parseResult.addParseError(exception.getLineNumber(), reason);
            log.debug("csv 파싱 실패: lineNo: {}, reason: {}", exception.getLineNumber(), reason);
        }
    }

    private String extractReason(CsvException exception) {
        Throwable cause = exception.getCause();
        if (cause != null && StringUtils.hasText(cause.getMessage())) {
            return cause.getMessage();
        }
        return "CSV 파싱 오류";
    }
}
