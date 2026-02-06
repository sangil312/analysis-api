package com.dev.analysis.domain.accesslog;

import java.util.List;
import java.util.Map;

public record TopPath(
        String path,
        Long count
) {
    public static List<TopPath> of(Map<String, Long> pathCount, int limit) {
        return pathCount.entrySet().stream()
                .sorted((a, b) -> Long.compare(b.getValue(), a.getValue()))
                .limit(limit)
                .map(entry -> new TopPath(entry.getKey(), entry.getValue()))
                .toList();
    }
}
