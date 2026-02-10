package com.dev.analysis.repository;

import com.dev.analysis.domain.accesslog.AccessLogAnalysis;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class AccessLogAnalysisInMemoryRepository implements AccessLogAnalysisRepository {
    private final AtomicLong analysisId = new AtomicLong(0);
    private final ConcurrentHashMap<Long, AccessLogAnalysis> storage = new ConcurrentHashMap<>();

    @Override
    public AccessLogAnalysis save(AccessLogAnalysis accessLogAnalysis) {
        Long analysisId = this.analysisId.incrementAndGet();
        storage.put(analysisId, accessLogAnalysis);
        accessLogAnalysis.saved(analysisId);
        return accessLogAnalysis;
    }

    @Override
    public Optional<AccessLogAnalysis> findById(Long id) {
        AccessLogAnalysis accessLogAnalysis = storage.get(id);
        return accessLogAnalysis == null ? Optional.empty() : Optional.of(accessLogAnalysis);
    }
}
