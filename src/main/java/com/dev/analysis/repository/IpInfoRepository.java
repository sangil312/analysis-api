package com.dev.analysis.repository;

import com.dev.analysis.domain.ipinfo.IpInfo;

import java.util.List;

public interface IpInfoRepository {
    void saveAll(List<IpInfo> ipInfo);
}
