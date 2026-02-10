package com.dev.analysis.application.service.accesslog;

import com.dev.analysis.domain.accesslog.AccessLogAnalysis;
import com.dev.analysis.domain.accesslog.ParseError;
import com.dev.analysis.domain.accesslog.TopIp;
import com.dev.analysis.domain.accesslog.TopRequestUri;
import com.dev.analysis.domain.accesslog.TopStatusCode;
import com.dev.analysis.repository.AccessLogAnalysisRepository;
import com.dev.analysis.application.service.accesslog.dto.AccessLogParseResult;
import com.dev.analysis.support.error.ApiException;
import com.dev.analysis.support.error.ErrorType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AccessLogManager {
    private final AccessLogAnalysisRepository accessLogAnalysisRepository;

    public AccessLogAnalysis createAccessLogAnalysis(AccessLogParseResult parseResult) {
        AccessLogAnalysis accessLogAnalysis = AccessLogAnalysis.create(
                parseResult.getTotalRequests(),
                parseResult.getSuccess2xx(),
                parseResult.getRedirect3xx(),
                parseResult.getClient4xx(),
                parseResult.getServer5xx(),
                ParseError.of(parseResult.getParseErrorCount(), parseResult.getParseErrorSamples()),
                TopIp.of(parseResult.getIpCount(), 10),
                TopRequestUri.of(parseResult.getRequestUriCount(), 10),
                TopStatusCode.of(parseResult.getStatusCodeCount(), 10)
        );

        return accessLogAnalysisRepository.save(accessLogAnalysis);
    }

    public AccessLogAnalysis findAccessLogAnalysis(Long analysisId) {
        return accessLogAnalysisRepository.findById(analysisId)
                .orElseThrow(() -> new ApiException(ErrorType.NOT_FOUND_DATA));
    }
}
