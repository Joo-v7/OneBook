package com.onebook.frontapi.controller.dormantaccount;

import com.onebook.frontapi.dto.dooraymessage.AuthRequest;
import com.onebook.frontapi.dto.dooraymessage.AuthResponse;
import com.onebook.frontapi.service.dooraymessage.DoorayMessageService;
import com.onebook.frontapi.service.member.MemberService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Objects;

@RequiredArgsConstructor
@Controller
@RequestMapping("/dormant-account")
public class DormantAccountController {

    private final DoorayMessageService doorayMessageService;
    private final MemberService memberService;
    public final static String DORMANT_AUTH = "DORMANT_AUTH";

    // 휴면 계정 전환용 두레이 인증 폼
    @GetMapping
    public String dormantAccountAuthenticationForm(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Cookie[] cookies = request.getCookies();

        if(Objects.isNull(cookies)) {
            response.sendRedirect("/");
            return null;
        }

        boolean hasDormantAuth = false;
        for(Cookie c : cookies) {
            if(c.getName().equals(DORMANT_AUTH)) {
                hasDormantAuth=true;
                break;
            }
        }

        if(hasDormantAuth) {
            return "my/member/doorayAuth";
        }

        return "/";
    }

    // 휴면 해제
    @GetMapping("/reactivate")
    public String reactivateDormantAccount(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        String loginId = null;

        if(Objects.isNull(cookies)) {
            return "redirect:/error/401";
        }

        for(Cookie c : cookies) {
            if(c.getName().equals(DORMANT_AUTH)) {
                HttpSession session = request.getSession(false);
                loginId = session.getAttribute("loginId").toString();
                c.setMaxAge(0);
            }
        }

        if(memberService.modifyStatusToActive(loginId)) {
            return "redirect:/login";
        }

        return "redirect:/error/401";
    }
}
