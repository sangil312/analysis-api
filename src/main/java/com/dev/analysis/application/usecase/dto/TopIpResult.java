package com.dev.analysis.application.usecase.dto;

import com.dev.analysis.domain.accesslog.TopIp;
import com.dev.analysis.domain.ipinfo.IpInfo;
import com.dev.analysis.enums.ValueType;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public record TopIpResult(
        String ip,
        Long count,
        String country,
        String region,
        String asn,
        String isp
) {
    public static List<TopIpResult> of(List<TopIp> topIps, List<IpInfo> ipInfos) {
        if (ipInfos.isEmpty()) {
            return topIps.stream()
                    .map(it -> new TopIpResult(
                            it.ip(),
                            it.count(),
                            ValueType.UNKNOWN.name(),
                            ValueType.UNKNOWN.name(),
                            ValueType.UNKNOWN.name(),
                            ValueType.UNKNOWN.name())
                    )
                    .toList();
        }

        Map<String, TopIp> topIpMap = topIps.stream()
                .collect(Collectors.toMap(TopIp::ip, topIp -> topIp));

        return ipInfos.stream()
                .map(it -> new TopIpResult(
                        it.getIp(),
                        topIpMap.get(it.getIp()).count(),
                        it.getCountry(),
                        it.getRegion(),
                        it.getAsn(),
                        it.getIsp())
                )
                .toList();
    }
}
