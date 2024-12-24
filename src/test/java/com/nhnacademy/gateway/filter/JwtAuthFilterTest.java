package com.nhnacademy.gateway.filter;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.http.server.reactive.MockServerHttpResponse;
import org.springframework.mock.web.reactive.function.server.MockServerRequest;
import org.springframework.mock.web.server.MockServerWebExchange;
import org.springframework.web.server.ServerWebExchange;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class JwtAuthFilterTest {

    JwtAuthFilter authFilter = new JwtAuthFilter();

    @Test
    void testFilter(){

        MockServerHttpRequest request = MockServerHttpRequest.get("/some-path").build();
        MockServerHttpResponse response = new MockServerHttpResponse();
        ServerWebExchange exchange = Mockito.mock(ServerWebExchange.class);
        Mockito.when(exchange.getRequest()).thenReturn(request);
        Mockito.when(exchange.getResponse()).thenReturn(response);

        // 테스트 실행
        authFilter.filter(exchange, null).block();

        // 결과 검증
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }


}