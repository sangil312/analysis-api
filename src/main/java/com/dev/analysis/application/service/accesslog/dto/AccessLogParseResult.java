package com.dev.analysis.application.service.accesslog.dto;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Getter
public final class AccessLogParseResult {
    private final Map<String, Long> requestUriCount = new HashMap<>();
    private final Map<Integer, Long> statusCodeCount = new HashMap<>();
    private final Map<String, Long> ipCount = new HashMap<>();
    private final List<String> parseErrorSamples = new ArrayList<>();
    private long totalRequestCount;
    private long success2xxCount;
    private long redirect3xxCount;
    private long client4xxCount;
    private long server5xxCount;
    private long parseSuccessCount;
    private long parseErrorCount;

    public void addParseResult(String clientIp, String requestUri, Integer statusCode) {
        totalRequestCount++;
        parseSuccessCount++;
        requestUriCount.merge(requestUri, 1L, Long::sum);
        statusCodeCount.merge(statusCode, 1L, Long::sum);
        ipCount.merge(clientIp, 1L, Long::sum);

        HttpStatus httpStatus = Objects.requireNonNull(HttpStatus.resolve(statusCode));
        if (httpStatus.is2xxSuccessful()) {
            success2xxCount++;
        } else if (httpStatus.is3xxRedirection()) {
            redirect3xxCount++;
        } else if (httpStatus.is4xxClientError()) {
            client4xxCount++;
        } else if (httpStatus.is5xxServerError()) {
            server5xxCount++;
        }
    }

    public void addParseError(long lineNo, String reason) {
        totalRequestCount++;
        parseErrorCount++;
        if (parseErrorSamples.size() < 5) {
            parseErrorSamples.add("line: " + lineNo + ", reason: " + reason);
        }
    }
}
