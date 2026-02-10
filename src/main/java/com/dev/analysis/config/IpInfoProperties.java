package com.dev.analysis.config;

import jakarta.validation.constraints.NotBlank;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.validation.annotation.Validated;

import java.time.Duration;

@Validated
@ConfigurationProperties(prefix = "ipinfo")
public record IpInfoProperties(
    @NotBlank String baseUrl,
    String token,
    @DefaultValue("1") int retryCount,
    @DefaultValue("500") Duration retryDelayMillis,
    @DefaultValue("PT24H") Duration cacheTtl,
    @DefaultValue("100000") long cacheMaximumSize
) {
}
