package com.dev.analysis.application.service.accesslog;

import com.dev.analysis.domain.accesslog.AccessLogAnalysis;
import com.dev.analysis.application.service.accesslog.dto.AccessLogParseResult;
import com.dev.analysis.application.service.accesslog.parser.AccessLogParser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


@Service
@RequiredArgsConstructor
@Slf4j
public class AnalysisService {
    private final AnalysisValidator analysisValidator;
    private final AccessLogParser accessLogParser;
    private final AccessLogManager accessLogManager;

    public AccessLogAnalysis createAccessLogAnalysis(MultipartFile file) {
        analysisValidator.validateFile(file);

        AccessLogParseResult parseResult = accessLogParser.parse(file);

        return accessLogManager.createAccessLogAnalysis(parseResult);
    }

    public AccessLogAnalysis findAccessLogAnalysis(Long analysisId) {
        return accessLogManager.findAccessLogAnalysis(analysisId);
    }
}
