package com.dev.analysis.client.dto;

import com.dev.analysis.application.service.ipinfo.dto.IpInfoData;
import com.dev.analysis.enums.ValueType;

public record IpInfoResult(
        String country,
        String continent,
        String asn,
        String as_name
) {
    public IpInfoData toIpInfoData() {
        return new IpInfoData(
                ValueType.valueOrUnknown(country),
                ValueType.valueOrUnknown(continent),
                ValueType.valueOrUnknown(asn),
                ValueType.valueOrUnknown(as_name)
        );
    }

    public static IpInfoResult unknown() {
        return new IpInfoResult(
                ValueType.UNKNOWN.name(),
                ValueType.UNKNOWN.name(),
                ValueType.UNKNOWN.name(),
                ValueType.UNKNOWN.name()
        );
    }
}
