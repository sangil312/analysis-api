package com.dev.analysis.service.accesslog;

import com.dev.analysis.domain.accesslog.AccessLogAnalysis;
import com.dev.analysis.service.accesslog.dto.AccessLogParseResult;
import com.dev.analysis.parser.AccessLogParser;
import com.dev.analysis.service.ipinfo.IpInfoService;
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
    private final AccessLogCreator accessLogCreator;
    private final IpInfoService ipInfoService;

    public Long createAccessLogAnalysis(MultipartFile file) {
        analysisValidator.validateFile(file);

        AccessLogParseResult parseResult = accessLogParser.parse(file);

        AccessLogAnalysis accessLogAnalysis = accessLogCreator.createAccessLogAnalysis(parseResult);

        ipInfoService.createIpInfo(accessLogAnalysis.getId(), accessLogAnalysis.getTopIps());

        return accessLogAnalysis.getId();
    }
}
