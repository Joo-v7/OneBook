package com.onebook.frontapi.service.dooraymessage;

import com.onebook.frontapi.adaptor.dooraymessage.DoorayMessageAdaptor;

import com.onebook.frontapi.dto.dooraymessage.AuthResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Slf4j
@RequiredArgsConstructor
@Service
public class DoorayMessageService {

    private final DoorayMessageAdaptor doorayMessageAdaptor;
    private final RedisTemplate<String, Object> redisTemplate;
    private String DOORAY_AUTH = "Dooray:";

    /**
     * 두레이로 인증번호(6자리) 보내기.
     */
    public AuthResponse requestDoorayMessage(HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            // Dooray Message 인증용 세션, 쿠키 발급.
            HttpSession session = request.getSession(true);
            Cookie cookie = new Cookie("DOORAY_AUTH", session.getId());
            cookie.setHttpOnly(true);
            cookie.setMaxAge(180); // 3분 뒤 삭제.
            cookie.setPath("/");
            response.addCookie(cookie);

            // 인증용 랜덤 6자리 숫자.
            String code = generateAuthCode();

            // redis에 저장, 저장 시간 3분
            redisTemplate.opsForValue().set(DOORAY_AUTH + session.getId(), code, 3, TimeUnit.MINUTES);

            // 두레이 메신저로 인증 code 전송.
            doorayMessageAdaptor.send(code);

            return new AuthResponse(true, "인증번호가 전송되었습니다");
        }catch(Exception e) {
            return new AuthResponse(false, "인증번호 발급 실패");
        }
    }

    /**
     * 인증번호 검증.
     */
    public AuthResponse validateAuthCode(HttpServletRequest request, String code) throws Exception {

        String sessionId = null;
        Cookie[] cookies = request.getCookies();
        for(Cookie c : cookies) {
             if(c.getName().equals("DOORAY_AUTH")) {
                 sessionId = c.getValue();
             }
        }

        if(Objects.isNull(sessionId)) {
            throw new BadRequestException();
        }

        String authCode = String.valueOf(redisTemplate.opsForValue().get(DOORAY_AUTH+sessionId));

        if(code.equals(authCode)) {
            return new AuthResponse(true, "인증 성공");
        }

        return new AuthResponse(false, "인증 실패");

    }

    // 인증번호 6자리 랜덤 생성.
    public String generateAuthCode() {
        Random random = new Random();
        int code = 100000 + random.nextInt(900000);
        return String.valueOf(code);
    }
}
