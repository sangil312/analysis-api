package com.dev.analysis.controller.accesslog.response;

import com.dev.analysis.application.usecase.dto.AccessLogAnalysisResult;
import com.dev.analysis.application.usecase.dto.TopIpResult;
import com.dev.analysis.domain.accesslog.ParseError;
import com.dev.analysis.domain.accesslog.TopRequestUri;
import com.dev.analysis.domain.accesslog.TopStatusCode;

import java.util.List;

public record AnalysisResponse(
        Long totalRequestCount,
        Double successRatio,
        Double redirectRatio,
        Double clientErrorRatio,
        Double serverErrorRatio,
        ParseError parseError,
        List<TopRequestUri> topRequestUris,
        List<TopStatusCode> topStatusCodes,
        List<TopIpResult> topIps
) {
    public static AnalysisResponse of(AccessLogAnalysisResult analysisResult) {
        return new AnalysisResponse(
                analysisResult.totalRequestCount(),
                analysisResult.successRatio(),
                analysisResult.redirectRatio(),
                analysisResult.clientErrorRatio(),
                analysisResult.serverErrorRatio(),
                analysisResult.parseError(),
                analysisResult.topRequestUris(),
                analysisResult.topStatusCodes(),
                analysisResult.topIps()
        );
    }
}
