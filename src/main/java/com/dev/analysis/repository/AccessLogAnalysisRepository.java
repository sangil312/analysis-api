package com.dev.analysis.repository;


import com.dev.analysis.domain.accesslog.AccessLogAnalysis;

import java.util.Optional;

public interface AccessLogAnalysisRepository {
    AccessLogAnalysis save(AccessLogAnalysis accessLogAnalysis);
    Optional<AccessLogAnalysis> findById(Long id);
}
