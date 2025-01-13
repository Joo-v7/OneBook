package com.nhnacademy.gateway.filter;

import com.nhnacademy.gateway.parser.OneBookJwtParser;
import io.jsonwebtoken.*;
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

    private final OneBookJwtParser oneBookJwtParser;

    public JwtAuthFilter(OneBookJwtParser oneBookJwtParser) {
        this.oneBookJwtParser = oneBookJwtParser;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String authHeader = exchange.getRequest().getHeaders().getFirst("Authorization");

        String path = exchange.getRequest().getPath().toString();

        if (path.equals("/auth/jwt") ||
                path.startsWith("/task/auth") ||
                // 회원가입 뚫어놈
                (path.equals("/task/members") && exchange.getRequest().getMethod().name().equals("POST")) ||
                path.startsWith("/task/carts/") ||
                path.startsWith("/task/book") ||
                path.startsWith("/task/image") ||
                (path.startsWith("/task/members/status")) ||
                (path.startsWith("/task/categories/topCategories")) ||
                (path.startsWith("/task/categories"))
        ) {
            return chain.filter(exchange);
        }
        // 토큰 형식 검사 예시
        // 테스트 주석
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        String token = authHeader.substring(7).trim();
        // 실제 JWT 검증 로직 필요 (signature, 만료시간, 클레임 등)

        String id = null;
        String role = null;
        try{
            id = validateToken(token);
            role = getRoleFromToken(token);

            // 경로가 /admin 인데 jwt token의 role이 ADMIN이 아니면 접근 불가.
            if(path.startsWith("/task/admin")) {
                if(!role.equals("ADMIN")) {
                    return handleUnauthorized(exchange, "Access denied for non-ADMIN role");
                }
            }

            String finalId = id;
            exchange = exchange.mutate()
                    .request(builder -> builder.header("X-MEMBER-ID", finalId))
                    .build();
            // 여기서 java.lang.ClassCastException: class java.lang.Long cannot be cast to class java.lang.String (java.lang.Long and java.lang.String are in module java.base of loader 'bootstrap') 뜸
        }catch (ExpiredJwtException e) {
            return handleUnauthorized(exchange, "JWT token is expired");
        } catch (UnsupportedJwtException e) {
            return handleUnauthorized(exchange, "JWT token is unsupported");
        } catch (MalformedJwtException e) {
            return handleUnauthorized(exchange, "Malformed JWT token");
        } catch (Exception e) {
            System.out.println("e :  "+ e.getMessage());
            return handleUnauthorized(exchange, "Invalid JWT token");
        }

        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return -1; // 우선순위를 높게 주고 싶다면 음수 값 지정
    }

    // role 정보 추출
    private String getRoleFromToken(String token) {
        Claims claims = oneBookJwtParser.getJwtParser().parseClaimsJws(token).getBody();
        return claims.get("role", String.class);
    }

    private String  validateToken(String token) {
        // TODO jwt 검증
        System.out.println("token : {}" + token);

        Claims body = oneBookJwtParser.getJwtParser().
                parseClaimsJws(token)
                .getBody();


        if( body != null){
            System.out.println("body !! " + body.toString());
            return body.get("id").toString();
        }

        throw new RuntimeException();
    }

    private Mono<Void> handleUnauthorized(ServerWebExchange exchange, String message){
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        exchange.getResponse().getHeaders().add("Content-Type", "application/json");
        String response = "{\"error\": \"Unauthorized\", \"message\": \"" + message + "\"}";

        return exchange.getResponse().writeWith(
                Mono.just(exchange.getResponse().bufferFactory().wrap(response.getBytes()))
        );
    }
}
