package com.dev.analysis.controller.v1.response;

public record CreateAnalysisResponse(
    Long analysisId
) {
    public static CreateAnalysisResponse of(Long analysisId) {
        return new CreateAnalysisResponse(analysisId);
    }
}
