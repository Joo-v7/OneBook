package com.onebook.frontapi.auth.handler;

import com.onebook.frontapi.auth.exception.AccessNotFoundException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import java.io.IOException;
import static com.onebook.frontapi.controller.dormantaccount.DormantAccountController.DORMANT_AUTH;

@Slf4j
public class AuthFailureHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        // 휴면 계정이면 10분짜리 쿠키 생성 후, 휴면 계정 인증 페이지로 이동.
        if(exception.getCause() instanceof AccessNotFoundException) {
            String errorMessage = exception.getMessage();
            String[] errorMessages = errorMessage.split(":");
            String loginId = errorMessages[0];

            HttpSession session = request.getSession(true);
            session.setAttribute("loginId", loginId);

            Cookie cookie = new Cookie(DORMANT_AUTH, session.getId());
            cookie.setHttpOnly(true);
            cookie.setMaxAge(5*60); // 휴면 인증용 쿠키 5분.
            cookie.setPath("/");
            response.addCookie(cookie);

            response.sendRedirect("/dormant-account");
            return;
        }

        // 그 외 나머지
        response.sendRedirect("/login");
    }
}
