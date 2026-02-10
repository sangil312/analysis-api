package com.dev.analysis.domain.accesslog;

import java.util.List;
import java.util.Map;

public record TopRequestUri(
        String path,
        Long count
) {
    public static List<TopRequestUri> of(Map<String, Long> pathCount, int limit) {
        return pathCount.entrySet().stream()
                .sorted((a, b) -> Long.compare(b.getValue(), a.getValue()))
                .limit(limit)
                .map(entry -> new TopRequestUri(entry.getKey(), entry.getValue()))
                .toList();
    }
}
