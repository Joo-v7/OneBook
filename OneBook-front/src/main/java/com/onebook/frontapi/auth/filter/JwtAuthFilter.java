package com.onebook.frontapi.auth.filter;

import com.onebook.frontapi.auth.dto.MemberInfoResponse;
import com.onebook.frontapi.feign.auth.AuthFeignClient;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private  final AuthFeignClient authFeignClient;

    // JWT 기반 인증: Authorization 쿠키에 있는 jwt 토큰 가져다가 검증함.
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info("JWT 인증 필터 실행");
        Cookie[] cookies = request.getCookies();

        // 쿠키가 없으면 여기서 걸려서 홈페이지에 쿠키 없이 접근하면 login 페이지로 이동함. 정적 리소스도 다음 필터로 넘김.
        // -> 해결: 쿠키가 없으면 JWT 필터 태우지 않고, 그냥 넘김.
        if(Objects.isNull(cookies) || Arrays.stream(cookies).noneMatch(e -> e.getName().equals("Authorization")) || isStaticResource(request)) {
            filterChain.doFilter(request, response);
            return;
        }

        for(Cookie c : cookies) {
            if(c.getName().equals("Authorization")) {
                log.info("JWT 토큰을 auth로 보내서 토큰 안에 있는 정보 가져옴");
               MemberInfoResponse memberInfoResponse = authFeignClient.getInfoByAuthorization("Bearer " + c.getValue());

               Authentication authentication = new UsernamePasswordAuthenticationToken(
                       memberInfoResponse.loginId(),
                       null,
                       List.of(new SimpleGrantedAuthority("ROLE_" + memberInfoResponse.role()))
               );

               SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        filterChain.doFilter(request, response);

    }

    // 정적 리소스 경로인지 확인하는 메서드
    private boolean isStaticResource(HttpServletRequest request) {
        String path = request.getRequestURI();
        return path.startsWith("/css/") || path.startsWith("/js/") || path.startsWith("/images/") || path.startsWith("/public/") || path.equals("/style.css");
    }

}