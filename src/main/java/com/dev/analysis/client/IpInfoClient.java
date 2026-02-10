package com.dev.analysis.client;

import com.dev.analysis.client.dto.IpInfoResult;
import com.dev.analysis.config.IpInfoProperties;
import com.dev.analysis.application.service.ipinfo.IpInfoPort;
import com.dev.analysis.application.service.ipinfo.dto.IpInfoData;
import com.github.benmanes.caffeine.cache.Cache;
import io.netty.channel.ConnectTimeoutException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.circuitbreaker.ReactiveCircuitBreaker;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.util.concurrent.TimeoutException;

@Component
@RequiredArgsConstructor
@Slf4j
public class IpInfoClient implements IpInfoPort {
    private final WebClient webClient;
    private final IpInfoProperties properties;
    private final Cache<String, IpInfoData> ipInfoCache;
    private final ReactiveCircuitBreaker circuitBreaker;

    @Override
    public IpInfoData getIpInfo(String ip) {
        IpInfoData cached = ipInfoCache.getIfPresent(ip);
        if (cached != null) {
            return cached;
        }

        IpInfoResult ipInfoResult = requestIpInfo(ip);
        IpInfoData ipInfoData = ipInfoResult.toIpInfoData();

        ipInfoCache.put(ip, ipInfoData);

        return ipInfoData;
    }

    private IpInfoResult requestIpInfo(String ip) {
        String requestUri = UriComponentsBuilder.fromUriString(properties.baseUrl())
                .pathSegment(ip)
                .queryParam("token", properties.token())
                .build()
                .toUriString();

        try {
            Mono<IpInfoResult> request = webClient.get()
                    .uri(requestUri)
                    .accept(MediaType.APPLICATION_JSON)
                    .exchangeToMono(this::responseHandler)
                    .retryWhen(Retry.fixedDelay(properties.retryCount(), properties.retryDelayMillis())
                            .filter(this::isTimeout));

            return circuitBreaker.run(request, throwable -> exceptionHandler(throwable, ip))
                    .block();

        } catch (Exception e) {
            log.error("[외부 API] ipinfo 요청 실패 baseUrl: {}, Exception: {}",
                    properties.baseUrl(), e.getMessage(), e);

            return IpInfoResult.unknown();
        }
    }

    private Mono<IpInfoResult> responseHandler(ClientResponse response) {
        if (response.statusCode().is2xxSuccessful()) {
            return response.bodyToMono(IpInfoResult.class);
        }

        if (response.statusCode().is4xxClientError()) {
            return Mono.just(IpInfoResult.unknown());
        }

        return response.createException().flatMap(Mono::error);
    }

    private Mono<IpInfoResult> exceptionHandler(Throwable e, String ip) {
        log.error("[외부 API] ipinfo 요청 실패 baseUrl: {}, ip: {}, Exception: {}",
                properties.baseUrl(), ip, e.getMessage(), e);

        return Mono.just(IpInfoResult.unknown());
    }

    private boolean isTimeout(Throwable e) {
        return e instanceof ConnectTimeoutException || e instanceof TimeoutException;
    }
}
