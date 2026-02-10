package com.dev.analysis.domain.accesslog;

import java.util.List;
import java.util.Map;

public record TopStatusCode(
        Integer statusCode,
        Long count
) {
    public static List<TopStatusCode> of(Map<Integer, Long> statusCount, int limit) {
        return statusCount.entrySet().stream()
                .sorted((a, b) -> Long.compare(b.getValue(), a.getValue()))
                .limit(limit)
                .map(entry -> new TopStatusCode(entry.getKey(), entry.getValue()))
                .toList();
    }
}
