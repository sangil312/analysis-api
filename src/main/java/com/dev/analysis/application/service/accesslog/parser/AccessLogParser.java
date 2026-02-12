package com.dev.analysis.application.service.accesslog.parser;

import com.dev.analysis.application.service.accesslog.AnalysisValidator;
import com.dev.analysis.support.error.ApiException;
import com.dev.analysis.support.error.ErrorType;
import com.opencsv.bean.CsvToBean;
import com.dev.analysis.application.service.accesslog.dto.AccessLogParseResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
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
    private final CsvToBeanFactory csvToBeanFactory;
    private final AnalysisValidator analysisValidator;

    public AccessLogParseResult parse(MultipartFile file) {
        log.info("csv 분석 시작: 파일명: {}, 파일 사이즈: {}byte", file.getOriginalFilename(), file.getSize());

        AccessLogParseResult parseResult = new AccessLogParseResult();

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))
        ) {
            AccessLogCsvExceptionHandler parseErrorHandler = new AccessLogCsvExceptionHandler(analysisValidator, parseResult);

            CsvToBean<AccessLogCsvRow> csvRows = csvToBeanFactory.create(AccessLogCsvRow.class, reader, parseErrorHandler);

            for (AccessLogCsvRow row : csvRows) {
                parseResult.addParseResult(row.getClientIp(), row.getRequestUri(), row.getHttpStatus());
                analysisValidator.validateFileLine(parseResult.getTotalRequestCount());
            }

            if (parseResult.getParseSuccessCount() == 0) {
                throw new ApiException(ErrorType.INVALID_DATA);
            }

            log.info("csv 분석 종료: 파일명: {}, 파일 사이즈: {}byte, 파싱 실패 line 수: {}",
                    file.getOriginalFilename(), file.getSize(), parseResult.getParseErrorCount());

            return parseResult;
        } catch (IOException e) {
            log.error("csv 파일 읽기 중 오류 발생 Exception: {}", e.getMessage(), e);
            throw new ApiException(ErrorType.SERVER_ERROR);
        }
    }
}
