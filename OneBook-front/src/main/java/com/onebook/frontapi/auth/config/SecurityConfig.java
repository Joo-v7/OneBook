package com.onebook.frontapi.auth.config;

import com.onebook.frontapi.auth.filter.JwtAuthFilter;
import com.onebook.frontapi.auth.handler.AuthFailureHandler;
import com.onebook.frontapi.auth.handler.AuthSuccessHandler;
import com.onebook.frontapi.auth.handler.CustomAccessDeniedHandler;
import com.onebook.frontapi.auth.handler.CustomAuthenticationEntryPoint;
import com.onebook.frontapi.feign.auth.AuthFeignClient;
import com.onebook.frontapi.feign.member.MemberClient;
import com.onebook.frontapi.service.cart.CartService;
import com.onebook.frontapi.service.member.MemberService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@Slf4j
public class SecurityConfig {

    private final AuthFeignClient authFeignClient;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
    private final MemberClient memberClient;
    private final CartService cartService;

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.ignoring()
                .requestMatchers(
                        "/css/**", "/js/**", "/images/**", "/public/**", "/fonts/**", "/style.css"
                );
    }

    @Bean
    public SecurityFilterChain devSecurityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable);

//        http.sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(
//                SessionCreationPolicy.STATELESS));


        http.formLogin(formLogin ->
                formLogin
                        .loginPage("/login") // 사용자 정의 로그인 페이지
                        .usernameParameter("id") // 사용자명 파라미터 이름
                        .passwordParameter("password") // 비밀번호 파라미터 이름
                        .loginProcessingUrl("/login/process") // 로그인 처리 URL
                        .successHandler(new AuthSuccessHandler(authFeignClient, memberClient, cartService))// jwt token 추가하기
                        .failureHandler(new AuthFailureHandler())
                        .permitAll() // 로그인 페이지 접근 허용
        );

        http.authorizeHttpRequests(authRequest -> {
            authRequest
                    .requestMatchers(
                            "/", "/login", "/public/**",
                            "/join", "/test/**",
                            "/dormant-account/**", "/dooray-message-authentication",
                            "/cart", "/book/**", "/review/**", "/front/reviews/**", "/like/**",
                            "/book-categories/**").permitAll()
                    .requestMatchers("/admin/**").hasRole("ADMIN") // 로그인할 때 Authentication을 생성하는데 이때 ROLE을 넣어줬음. 그래서 이렇게 사용 가능.
                    .anyRequest().authenticated();
        });

        http.addFilterBefore(new JwtAuthFilter(authFeignClient), UsernamePasswordAuthenticationFilter.class);

        // logout 시 쿠키 삭제하기
        http.logout(logout -> {
            logout.logoutUrl("/logout").
                    deleteCookies("Authorization")
                    .logoutSuccessUrl("/"); // 로그아웃 성공 핸들러 추가
        });

        http.exceptionHandling(exceptionHandling ->
                exceptionHandling
                        .accessDeniedHandler(customAccessDeniedHandler)
//                        .authenticationEntryPoint(customAuthenticationEntryPoint)
        );

        return http.build();
    }

    // Password Encoder, InMemory is Dev
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // BCrypt 암호화 사용
    }

}
