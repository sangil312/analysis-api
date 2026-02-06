package com.dev.analysis.client.dto;

import com.dev.analysis.service.ipinfo.dto.IpInfoData;
import org.springframework.util.StringUtils;

public record IpInfoResult(
        String country,
        String continent,
        String asn,
        String as_name
) {
    public IpInfoData toIpInfoData() {
        return new IpInfoData(
                valueOrUnknown(this.country),
                valueOrUnknown(this.continent),
                valueOrUnknown(this.asn),
                valueOrUnknown(this.as_name)
        );
    }

    public static IpInfoResult unknown() {
        return new IpInfoResult("UNKNOWN", "UNKNOWN", "UNKNOWN", "UNKNOWN");
    }

    private String valueOrUnknown(String value) {
        return StringUtils.hasText(value) ? value : "UNKNOWN";
    }
}
