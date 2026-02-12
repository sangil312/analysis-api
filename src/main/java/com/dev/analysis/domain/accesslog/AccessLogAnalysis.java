package com.dev.analysis.domain.accesslog;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AccessLogAnalysis {
    private Long id;
    private Long totalRequestCount;
    private Double successRatio;
    private Double redirectRatio;
    private Double clientErrorRatio;
    private Double serverErrorRatio;
    private ParseError parseError;
    private List<TopIp> topIps;
    private List<TopRequestUri> topRequestUris;
    private List<TopStatusCode> topStatusCodes;

    public static AccessLogAnalysis create(
        Long totalRequestCount,
        Long successCount,
        Long redirectCount,
        Long clientServerCount,
        Long serverErrorCount,
        ParseError parseError,
        List<TopIp> topIps,
        List<TopRequestUri> topRequestUris,
        List<TopStatusCode> topStatusCodes
    ) {
        AccessLogAnalysis accessLogAnalysis = new AccessLogAnalysis();
        accessLogAnalysis.totalRequestCount = totalRequestCount;
        accessLogAnalysis.successRatio = toRatio(successCount, totalRequestCount);
        accessLogAnalysis.redirectRatio = toRatio(redirectCount, totalRequestCount);
        accessLogAnalysis.clientErrorRatio = toRatio(clientServerCount, totalRequestCount);
        accessLogAnalysis.serverErrorRatio = toRatio(serverErrorCount, totalRequestCount);
        accessLogAnalysis.parseError = parseError;
        accessLogAnalysis.topIps = topIps;
        accessLogAnalysis.topRequestUris = topRequestUris;
        accessLogAnalysis.topStatusCodes = topStatusCodes;
        return accessLogAnalysis;
    }

    public void saved(Long analysisId) {
        this.id = analysisId;
    }

    private static Double toRatio(long part, long total) {
        if (total == 0) {
            return 0.0;
        }
        double value = (part * 100.0) / total;
        return Math.round(value * 100.0) / 100.0;
    }
}
