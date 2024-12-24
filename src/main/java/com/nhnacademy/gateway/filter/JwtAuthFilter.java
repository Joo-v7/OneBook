package com.nhnacademy.gateway.filter;

import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * jwt token을 인가하는 filter임
 * /auth/login 은 jwt token을 발행할 수 있는지 확인하는 곳이라
 * 필터링 안되게 해놓음
 *
 * 수정자 : 문영호
 *
 */
@Component
public class JwtAuthFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String authHeader = exchange.getRequest().getHeaders().getFirst("Authorization");

        // jwt token 받는거 확인
        if (exchange.getRequest().getPath().toString().equals("/auth/jwt")) {
            return chain.filter(exchange);
        }


        // 토큰 형식 검사 예시
        // 테스트 주석
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

        // header에 추가하기
        exchange = exchange.mutate()
                .request(builder -> builder.header("X-USER-ID", "test"))
                .build();

        // 여기에 하면 되려나

        // 토큰 유효하면 다음 필터로 넘어감
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return -1; // 우선순위를 높게 주고 싶다면 음수 값 지정
    }

    private boolean validateToken(String token) {
        // TODO jwt 검증
        System.out.println("token : {}" + token);

        // JWT 검증 로직 구현 필요
        return true;
    }
}
