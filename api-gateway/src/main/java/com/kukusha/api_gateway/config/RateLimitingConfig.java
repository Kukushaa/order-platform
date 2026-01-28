package com.kukusha.api_gateway.config;

import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.InetSocketAddress;

@Configuration
public class RateLimitingConfig {
    @Bean
    KeyResolver ipKeyResolver() {
        return exchange -> Mono.justOrEmpty(remoteIp(exchange)).defaultIfEmpty("unknown");
    }

    private String remoteIp(ServerWebExchange webExchange) {
        InetSocketAddress address = webExchange.getRequest().getRemoteAddress();

        return address != null && address.getAddress() != null ? address.getAddress().getHostAddress() : null;
    }
}
