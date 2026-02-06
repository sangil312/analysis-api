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
    private Double successCount;
    private Double redirectCount;
    private Double clientErrorCount;
    private Double serverErrorCount;
    private Long parseErrorCount;
    private List<String> parseErrorSamples;
    private List<TopIp> topIps;
    private List<TopPath> topPaths;
    private List<TopStatusCode> topStatusCodes;

    public static AccessLogAnalysis create(
        Long totalRequestCount,
        Long successCount,
        Long redirectCount,
        Long clientServerCount,
        Long serverErrorCount,
        Long parseErrorCount,
        List<String> parseErrorSamples,
        List<TopIp> topIps,
        List<TopPath> topPaths,
        List<TopStatusCode> topStatusCodes
    ) {
        AccessLogAnalysis accessLogAnalysis = new AccessLogAnalysis();
        accessLogAnalysis.totalRequestCount = totalRequestCount;
        accessLogAnalysis.successCount = toRatio(successCount, totalRequestCount);
        accessLogAnalysis.redirectCount = toRatio(redirectCount, totalRequestCount);
        accessLogAnalysis.clientErrorCount = toRatio(clientServerCount, totalRequestCount);
        accessLogAnalysis.serverErrorCount = toRatio(serverErrorCount, totalRequestCount);
        accessLogAnalysis.parseErrorCount = parseErrorCount;
        accessLogAnalysis.parseErrorSamples = parseErrorSamples;
        accessLogAnalysis.topIps = topIps;
        accessLogAnalysis.topPaths = topPaths;
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
