package com.example.gateway.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.OrderedGatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class GlobalFilter extends AbstractGatewayFilterFactory<GlobalFilter.Config> {

    public GlobalFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return new OrderedGatewayFilter(new GatewayFilter() {

            @Override
            public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

                // webflux
                ServerHttpRequest request = exchange.getRequest();
                ServerHttpResponse response = exchange.getResponse();

                log.info("{} Custom PRE filter: request uri -> {} ", config.baseMessage, request.getId());

                // other thread
                Mono<Void> objectMono = Mono.fromRunnable(() -> {
                    log.info("{} Custom POST filter: response code -> {} ", config.baseMessage, response.getStatusCode());
                });
                Mono<Void> filter = chain.filter(exchange).then(objectMono);
                return filter;
            }
        }, Ordered.HIGHEST_PRECEDENCE);
    }

    public static class Config {
        // Put the Configuration properties
        private String baseMessage;
    }
}
