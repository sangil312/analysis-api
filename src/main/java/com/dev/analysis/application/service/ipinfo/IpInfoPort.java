package com.dev.analysis.application.service.ipinfo;

import com.dev.analysis.application.service.ipinfo.dto.IpInfoData;

public interface IpInfoPort {
    IpInfoData getIpInfo(String ip);
}
