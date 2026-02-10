package com.dev.analysis.service.accesslog.dto;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Getter
public class AccessLogParseResult {
    private final int maxParseErrorSamples;
    private final Map<String, Long> pathCount = new HashMap<>();
    private final Map<Integer, Long> statusCount = new HashMap<>();
    private final Map<String, Long> ipCount = new HashMap<>();
    private final List<String> parseErrorSamples = new ArrayList<>();
    private long totalRequests;
    private long success2xx;
    private long redirect3xx;
    private long client4xx;
    private long server5xx;
    private long parseErrorCount;

    public AccessLogParseResult(int maxParseErrorSamples) {
        this.maxParseErrorSamples = maxParseErrorSamples;
    }

    public void addParseResult(String clientIp, String requestUri, Integer statusCode) {
        totalRequests++;
        pathCount.merge(requestUri, 1L, Long::sum);
        statusCount.merge(statusCode, 1L, Long::sum);
        ipCount.merge(clientIp, 1L, Long::sum);

        HttpStatus httpStatus = Objects.requireNonNull(HttpStatus.resolve(statusCode));
        if (httpStatus.is2xxSuccessful()) {
            success2xx++;
        } else if (httpStatus.is3xxRedirection()) {
            redirect3xx++;
        } else if (httpStatus.is4xxClientError()) {
            client4xx++;
        } else if (httpStatus.is5xxServerError()) {
            server5xx++;
        }
    }

    public boolean checkResult() {
        return totalRequests > 0;
    }

    public void addParseError(long lineNo, String reason) {
        totalRequests++;
        parseErrorCount++;
        if (parseErrorSamples.size() < maxParseErrorSamples) {
            parseErrorSamples.add("line: " + lineNo + ", reason: " + reason);
        }
    }
}
