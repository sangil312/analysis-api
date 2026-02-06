package com.dev.analysis.domain.accesslog;

import java.util.List;
import java.util.Map;

public record TopIp(
        String ip,
        Long count
) {
    public static List<TopIp> of(Map<String, Long> ipCount, int limit) {
        return ipCount.entrySet().stream()
                .sorted((a, b) -> Long.compare(b.getValue(), a.getValue()))
                .limit(limit)
                .map(entry -> new TopIp(entry.getKey(), entry.getValue()))
                .toList();
    }
}
