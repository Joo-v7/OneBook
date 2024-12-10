package com.nhnacademy.gateway.filter;

import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class JwtAuthFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String authHeader = exchange.getRequest().getHeaders().getFirst("Authorization");

        // 토큰 형식 검사 예시
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        String token = authHeader.substring(7);
        // 실제 JWT 검증 로직 필요 (signature, 만료시간, 클레임 등)
        if(!validateToken(token)) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        // 토큰 유효하면 다음 필터로 넘어감
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return -1; // 우선순위를 높게 주고 싶다면 음수 값 지정
    }

    private boolean validateToken(String token) {
        // JWT 검증 로직 구현 필요
        return true;
    }
}
