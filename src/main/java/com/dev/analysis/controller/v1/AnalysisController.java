package com.dev.analysis.controller.v1;

import com.dev.analysis.controller.v1.response.ApiResponse;
import com.dev.analysis.controller.v1.response.CreateAnalysisResponse;
import com.dev.analysis.service.accesslog.AnalysisService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
public class AnalysisController {
    private final AnalysisService analysisService;

    @PostMapping(value = "/analysis", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<CreateAnalysisResponse> analysis(
            @RequestParam("file") MultipartFile file
    ) {
        return ApiResponse.success(
                CreateAnalysisResponse.of(analysisService.createAccessLogAnalysis(file))
        );
    }
}
