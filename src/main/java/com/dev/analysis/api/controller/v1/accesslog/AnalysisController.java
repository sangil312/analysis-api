package com.dev.analysis.api.controller.v1.accesslog;

import com.dev.analysis.api.config.ErrorDocumented;
import com.dev.analysis.api.controller.response.Response;
import com.dev.analysis.application.usecase.AnalysisUseCase;
import com.dev.analysis.api.controller.v1.accesslog.response.AnalysisResponse;
import com.dev.analysis.api.controller.v1.accesslog.response.CreateAnalysisResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import static com.dev.analysis.support.error.ErrorType.EMPTY_FILE;
import static com.dev.analysis.support.error.ErrorType.INVALID_DATA;
import static com.dev.analysis.support.error.ErrorType.MAX_FILE_LINE_OVER;
import static com.dev.analysis.support.error.ErrorType.MAX_FILE_SIZE_OVER;
import static com.dev.analysis.support.error.ErrorType.NOT_FOUND_DATA;

@RestController
@RequiredArgsConstructor
@Tag(name = "Analysis API")
public class AnalysisController {
    private final AnalysisUseCase analysisUseCase;

    @Operation(summary = "CSV 분석 생성", description = "CSV 파일 분석 집계 생성")
    @ErrorDocumented({EMPTY_FILE, INVALID_DATA, MAX_FILE_SIZE_OVER, MAX_FILE_LINE_OVER})
    @PostMapping(value = "/v1/analysis", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Response<CreateAnalysisResponse> createAccessLogAnalysis(
            @RequestParam("file") MultipartFile file
    ) {
        var accessLogAnalysis = analysisUseCase.createAccessLogAnalysis(file);
        return Response.success(CreateAnalysisResponse.of(accessLogAnalysis.getId()));
    }

    @Operation(summary = "CSV 분석 결과 조회", description = "CSV 파일 분석 집계 조회")
    @ErrorDocumented({NOT_FOUND_DATA})
    @GetMapping("/v1/analysis/{analysisId}")
    public Response<AnalysisResponse> findAccessLogAnalysis(
            @PathVariable Long analysisId
    ) {
        var analysisResult = analysisUseCase.findAnalysis(analysisId);
        return Response.success(AnalysisResponse.of(analysisResult));
    }
}
