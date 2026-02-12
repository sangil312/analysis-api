package com.dev.analysis.controller.accesslog;

import com.dev.analysis.application.usecase.AnalysisUseCase;
import com.dev.analysis.controller.accesslog.response.AnalysisResponse;
import com.dev.analysis.controller.response.ApiResponse;
import com.dev.analysis.controller.accesslog.response.CreateAnalysisResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
public class AnalysisController {
    private final AnalysisUseCase analysisUseCase;

    @PostMapping(value = "/analysis", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<CreateAnalysisResponse> createAccessLogAnalysis(
            @RequestParam("file") MultipartFile file
    ) {
        var accessLogAnalysis = analysisUseCase.createAccessLogAnalysis(file);
        return ApiResponse.success(CreateAnalysisResponse.of(accessLogAnalysis.getId()));
    }

    @GetMapping("/analysis/{analysisId}")
    public ApiResponse<AnalysisResponse> findAccessLogAnalysis(
            @PathVariable Long analysisId
    ) {
        var analysisResult = analysisUseCase.findAnalysis(analysisId);
        return ApiResponse.success(AnalysisResponse.of(analysisResult));
    }
}
