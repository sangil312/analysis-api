package com.dev.analysis.application.service.ipinfo.dto;

public record IpInfoData(
        String country,
        String region,
        String asn,
        String isp
) {
}
