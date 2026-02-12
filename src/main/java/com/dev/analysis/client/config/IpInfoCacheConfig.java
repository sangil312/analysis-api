package com.dev.analysis.client.config;

import com.dev.analysis.application.service.ipinfo.dto.IpInfoData;
import com.dev.analysis.config.IpInfoProperties;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class IpInfoCacheConfig {

    @Bean
    public Cache<String, IpInfoData> ipInfoCache(IpInfoProperties properties) {
        return Caffeine.newBuilder()
                .maximumSize(properties.cacheMaximumSize())
                .expireAfterWrite(properties.cacheTtl())
                .recordStats()
                .build();
    }
}
