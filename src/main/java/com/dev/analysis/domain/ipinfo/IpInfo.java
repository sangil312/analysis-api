package com.dev.analysis.domain.ipinfo;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class IpInfo {
    private Long id;
    private Long analysisId;
    private String ip;
    private String country;
    private String region;
    private String asn;
    private String isp;

    public static IpInfo create(
            Long analysisId,
            String ip,
            String country,
            String region,
            String asn,
            String isp
    ) {
        IpInfo ipInfo = new IpInfo();
        ipInfo.analysisId = analysisId;
        ipInfo.ip = ip;
        ipInfo.country = country;
        ipInfo.region = region;
        ipInfo.asn = asn;
        ipInfo.isp = isp;

        return ipInfo;
    }
}