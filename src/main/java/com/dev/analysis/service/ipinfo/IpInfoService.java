package com.dev.analysis.service.ipinfo;

import com.dev.analysis.domain.ipinfo.IpInfo;
import com.dev.analysis.domain.accesslog.TopIp;
import com.dev.analysis.repository.IpInfoRepository;
import com.dev.analysis.service.ipinfo.dto.IpInfoData;
import com.dev.analysis.support.error.ApiException;
import com.dev.analysis.support.error.ErrorType;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class IpInfoService {
    private final IpInfoRepository ipInfoRepository;
    private final IpInfoPort ipInfoPort;

    @Async
    public void createIpInfo(Long analysisId, List<TopIp> topIps) {
        List<String> ips = topIps.stream()
                .map(TopIp::ip)
                .toList();

        if (ips.isEmpty()) {
            throw new ApiException(ErrorType.INVALID_REQUEST);
        }

        List<IpInfo> ipInfos = ips.stream()
                .map(ip -> {
                    IpInfoData ipInfoData = ipInfoPort.getIpInfo(ip);
                    return IpInfo.create(
                            analysisId,
                            ip,
                            ipInfoData.country(),
                            ipInfoData.region(),
                            ipInfoData.asn(),
                            ipInfoData.isp()
                    );
                })
                .toList();

        ipInfoRepository.saveAll(ipInfos);
    }
}
