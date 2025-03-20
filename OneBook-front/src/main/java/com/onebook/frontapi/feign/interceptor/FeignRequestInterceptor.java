package com.onebook.frontapi.feign.interceptor;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FeignRequestInterceptor implements RequestInterceptor {

    private final HttpServletRequest request;
    @Override
    public void apply(RequestTemplate requestTemplate) {

        Cookie[] cookies = request.getCookies();
        if(cookies != null){
            for(Cookie cookie : cookies){
                if("Authorization".equals(cookie.getName())){
                    String jwtToken = cookie.getValue();
                    requestTemplate.header("Authorization", "Bearer " + jwtToken);
                }
            }
        }

    }
}
