package com.dev.analysis.application.usecase.dto;

import com.dev.analysis.domain.accesslog.AccessLogAnalysis;
import com.dev.analysis.domain.accesslog.ParseError;
import com.dev.analysis.domain.accesslog.TopRequestUri;
import com.dev.analysis.domain.accesslog.TopStatusCode;
import com.dev.analysis.domain.ipinfo.IpInfo;

import java.util.List;

public record AccessLogAnalysisResult(
        Long analysisId,
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
    public static AccessLogAnalysisResult of(
            AccessLogAnalysis accessLogAnalysis,
            List<IpInfo> ipInfos
    ) {
        return new  AccessLogAnalysisResult(
                accessLogAnalysis.getId(),
                accessLogAnalysis.getTotalRequestCount(),
                accessLogAnalysis.getSuccessRatio(),
                accessLogAnalysis.getRedirectRatio(),
                accessLogAnalysis.getClientErrorRatio(),
                accessLogAnalysis.getServerErrorRatio(),
                accessLogAnalysis.getParseError(),
                accessLogAnalysis.getTopRequestUris(),
                accessLogAnalysis.getTopStatusCodes(),
                TopIpResult.of(accessLogAnalysis.getTopIps(), ipInfos)
        );
    }
}
