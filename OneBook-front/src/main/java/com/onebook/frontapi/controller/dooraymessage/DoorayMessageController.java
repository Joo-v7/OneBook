package com.onebook.frontapi.controller.dooraymessage;

import com.onebook.frontapi.dto.dooraymessage.AuthRequest;
import com.onebook.frontapi.dto.dooraymessage.AuthResponse;
import com.onebook.frontapi.service.dooraymessage.DoorayMessageService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/dooray-message-authentication")
public class DoorayMessageController {

    private final DoorayMessageService doorayMessageService;

    @GetMapping
    public ResponseEntity<AuthResponse> sendAuthenticationMessage(HttpServletRequest request, HttpServletResponse response) throws Exception {
        // Dooray 인증을 시도한 흔적(쿠키)가 있으면 쿠키가 사라질때까지 두레이 인증 서비스 이용 불가능
        Cookie[] cookies = request.getCookies();
        for(Cookie c : cookies) {
            if(c.getName().equals("DOORAY_AUTH")) {
                response.sendRedirect("/");
            }
        }

        AuthResponse result =  doorayMessageService.requestDoorayMessage(request, response);
        return ResponseEntity.ok().body(result);
    }

    @PostMapping
    public ResponseEntity<AuthResponse> validateAuthenticationMessage(@RequestBody AuthRequest code, HttpServletRequest request) throws Exception {
        AuthResponse result =  doorayMessageService.validateAuthCode(request, code.authCode());
        return ResponseEntity.ok().body(result);
    }

}
