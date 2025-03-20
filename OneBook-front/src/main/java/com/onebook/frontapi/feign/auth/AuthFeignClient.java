package com.onebook.frontapi.feign.auth;

import com.onebook.frontapi.auth.dto.MemberInfoResponse;
import com.onebook.frontapi.dto.auth.JwtLoginIdRequest;
import com.onebook.frontapi.dto.auth.TokenResponse;
import feign.Headers;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "auth-service", url = "${onebook.gatewayUrl}")
@Component
public interface AuthFeignClient {
    @GetMapping("/auth/jwt")
    ResponseEntity<TokenResponse> getJwtToken(@RequestParam("id") String id);

    @GetMapping("/auth/test")
    public String returnString();

    // jwt 토큰으로 멤버 정보(이름, 아이디, role) 가져옴.
    @GetMapping("/auth/my/info")
    MemberInfoResponse getInfoByAuthorization(@RequestHeader("Authorization") String authorization);


}