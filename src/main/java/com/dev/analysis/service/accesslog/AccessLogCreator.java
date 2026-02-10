package com.dev.analysis.service.accesslog;

import com.dev.analysis.domain.accesslog.AccessLogAnalysis;
import com.dev.analysis.domain.accesslog.TopIp;
import com.dev.analysis.domain.accesslog.TopPath;
import com.dev.analysis.domain.accesslog.TopStatusCode;
import com.dev.analysis.repository.AccessLogAnalysisRepository;
import com.dev.analysis.service.accesslog.dto.AccessLogParseResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AccessLogCreator {
    private final AccessLogAnalysisRepository accessLogAnalysisRepository;

    public AccessLogAnalysis createAccessLogAnalysis(AccessLogParseResult parseResult) {
        AccessLogAnalysis accessLogAnalysis = AccessLogAnalysis.create(
                parseResult.getTotalRequests(),
                parseResult.getSuccess2xx(),
                parseResult.getRedirect3xx(),
                parseResult.getClient4xx(),
                parseResult.getServer5xx(),
                parseResult.getParseErrorCount(),
                parseResult.getParseErrorSamples(),
                TopIp.of(parseResult.getIpCount(), 10),
                TopPath.of(parseResult.getPathCount(), 10),
                TopStatusCode.of(parseResult.getStatusCount(), 10)
        );

        return accessLogAnalysisRepository.save(accessLogAnalysis);
    }
}
