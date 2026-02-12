package com.dev.analysis.application.usecase;

import com.dev.analysis.application.service.accesslog.AnalysisService;
import com.dev.analysis.application.service.ipinfo.IpInfoService;
import com.dev.analysis.application.usecase.dto.AccessLogAnalysisResult;
import com.dev.analysis.domain.accesslog.AccessLogAnalysis;
import com.dev.analysis.domain.ipinfo.IpInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Component
@RequiredArgsConstructor
public class AnalysisUseCase {
    private final AnalysisService analysisService;
    private final IpInfoService ipInfoService;

    public AccessLogAnalysis createAccessLogAnalysis(MultipartFile file) {
        AccessLogAnalysis accessLogAnalysis = analysisService.createAccessLogAnalysis(file);

        ipInfoService.createIpInfo(accessLogAnalysis.getId(), accessLogAnalysis.getTopIps());

        return accessLogAnalysis;
    }

    public AccessLogAnalysisResult findAnalysis(Long analysisId) {
        AccessLogAnalysis accessLogAnalysis = analysisService.findAccessLogAnalysis(analysisId);

        List<IpInfo> ipInfos = ipInfoService.findIpInfos(analysisId);

        return AccessLogAnalysisResult.of(accessLogAnalysis, ipInfos);
    }
}
