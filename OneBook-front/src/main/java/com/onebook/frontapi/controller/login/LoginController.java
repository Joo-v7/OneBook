package com.onebook.frontapi.controller.login;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Objects;

@Controller
@RequestMapping("/login")
public class LoginController {

    @GetMapping
    public String loginForm(HttpServletRequest request, HttpServletResponse response) throws Exception {

        // jwt가 이미 있으면 홈으로.
        Cookie[] cookies = request.getCookies();
        for(Cookie c : cookies) {
            if(c.getName().equals("Authorization")) {
                response.sendRedirect("/");
            }
        }

        return "login/login";
    }

}
