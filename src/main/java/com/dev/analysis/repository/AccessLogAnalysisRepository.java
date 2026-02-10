package com.dev.analysis.repository;


import com.dev.analysis.domain.accesslog.AccessLogAnalysis;

public interface AccessLogAnalysisRepository {
    AccessLogAnalysis save(AccessLogAnalysis accessLogAnalysis);
}
