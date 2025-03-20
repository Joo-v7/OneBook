package com.nhnacademy.authentication.utils;

import com.nhnacademy.authentication.dev_Member.JwtMemberDto;
import com.nhnacademy.authentication.dto.TokenResponse;
import com.nhnacademy.authentication.properties.JwtProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtUtils {
    @Value("${jwt.secret}")
    private String secretKey;
    private final JwtProperties jwtProperties;

    public TokenResponse makeJwt(JwtMemberDto jwtMemberDto){

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.SECOND, jwtProperties.getExpirationTime());

        // TODO - jwt 합쳐지면 여기 수정
        SecretKey key = Keys.hmacShaKeyFor(jwtProperties.getSecret().getBytes());

        log.info("jwtMemberDto id : {}", jwtMemberDto.getId());

        String jwtToken = Jwts.builder()
                .claim("id", jwtMemberDto.getId())
                .claim("role", jwtMemberDto.getRole())
                .setIssuedAt(new Date())
                .setExpiration(calendar.getTime())
                .signWith(key)
                .compact();

        return new TokenResponse(jwtToken, jwtProperties.getTokenPrefix(), jwtProperties.getExpirationTime());
    }

    /**
     * JWT Token Parse.
     * @param authorization(JWT Token)
     * @return
     */
    public String parseAndReturnId(String authorization) {
        String token = validateAndExtractToken(authorization);
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(secretKey.getBytes())
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.get("id", String.class);
    }

    public String validateAndExtractToken(String authorization) {
        if (Objects.isNull(authorization) || !authorization.startsWith("Bearer ")) {
            throw new IllegalArgumentException("토큰 헤더가 유효하지않음. Bearer 로 시작해야함.");
        }
        return authorization.substring(7);
    }

}
