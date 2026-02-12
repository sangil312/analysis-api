package com.dev.analysis.api.controller.v1.accesslog.response;

public record CreateAnalysisResponse(
    Long analysisId
) {
    public static CreateAnalysisResponse of(Long analysisId) {
        return new CreateAnalysisResponse(analysisId);
    }
}
