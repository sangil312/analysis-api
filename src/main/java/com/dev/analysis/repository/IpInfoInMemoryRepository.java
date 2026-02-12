package com.dev.analysis.repository;

import com.dev.analysis.domain.ipinfo.IpInfo;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class IpInfoInMemoryRepository implements IpInfoRepository {
    private final ConcurrentHashMap<Long, List<IpInfo>> storage = new ConcurrentHashMap<>();

    @Override
    public void saveAll(List<IpInfo> ipInfos) {
        Long analysisId = ipInfos.getFirst().getAnalysisId();
        storage.put(analysisId, ipInfos);
    }

    @Override
    public List<IpInfo> findByAnalysisId(Long analysisId) {
        List<IpInfo> ipInfos = storage.get(analysisId);
        return ipInfos == null ? List.of() : ipInfos;
    }
}
