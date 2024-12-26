package com.nhnacademy.gateway.parser;

import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;

@Component
public class OneBookJwtParser {
    private final String stringKey;
    private final JwtParser jwtParser;

    public OneBookJwtParser(@Value("${jwt.secret}") String stringKey) {
        this.stringKey = stringKey;
        SecretKey key = Keys.hmacShaKeyFor(stringKey.getBytes());

        jwtParser = Jwts.parserBuilder()
                .setSigningKey(key)
                .build();
    }

    public JwtParser getJwtParser() {
        return jwtParser;
    }
}
