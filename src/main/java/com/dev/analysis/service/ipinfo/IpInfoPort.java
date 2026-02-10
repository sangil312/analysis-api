package com.dev.analysis.service.ipinfo;

import com.dev.analysis.service.ipinfo.dto.IpInfoData;

public interface IpInfoPort {
    IpInfoData getIpInfo(String ip);
}
