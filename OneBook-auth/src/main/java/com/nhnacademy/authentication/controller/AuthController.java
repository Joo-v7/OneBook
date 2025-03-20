package com.nhnacademy.authentication.controller;

import com.nhnacademy.authentication.adaptor.MemberAdaptor;
import com.nhnacademy.authentication.dev_Member.JwtMemberDto;
import com.nhnacademy.authentication.dto.*;
import com.nhnacademy.authentication.utils.JwtUtils;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Tag(name = "Authentication", description = "JWT 발급 및 파싱")
@RestController
@Slf4j
@RequiredArgsConstructor
public class AuthController {

    private final MemberAdaptor memberAdaptor;

    private final JwtUtils jwtUtils;

//    // 이거 안씀.
//    @PostMapping("/auth/login")
//    public ResponseEntity<TokenResponse> auth(@RequestBody LoginRequest loginRequest){
//        log.info("Login Request : {}", loginRequest);
//
//        MemberLoginDto memberLoginDto = new MemberLoginDto(loginRequest.getId(), loginRequest.getPassword());
//
//        ResponseEntity<Boolean> isValid = memberAdaptor.login(memberLoginDto);
//
//        log.info(" is valid {}", Objects.requireNonNull(isValid.getBody()).toString());
//
//        if(Boolean.TRUE.equals(isValid.getBody())){
//
////            return ResponseEntity.of(Optional.of(jwtUtils.makeJwt(memberLoginDto.loginId())));
//            return ResponseEntity.of(Optional.empty());
//        }
//
//        throw new RuntimeException("valid 하지 않음 TODO 바꾸기  ");
//    }

    // 이게 jwt 생성하는 요청인거 같음.

    /**
     * 흐름
     * 1. getMemberForJWT로 task에 있는 memberController에 요청함.
     * 2. 응답으로
     * @param id
     * @return
     */
    @GetMapping("/auth/jwt")
    public ResponseEntity<TokenResponse> getJwtToken(@RequestParam("id") String id){
        ResponseEntity<JwtMemberDto> memberForJWT = memberAdaptor.getMemberForJWT(id);
        TokenResponse tokenResponse = jwtUtils.makeJwt(memberForJWT.getBody());
        return ResponseEntity.of(Optional.of(tokenResponse));
    }

    @GetMapping("/auth/my/info")
    public ResponseEntity<MemberInfoResponse> getInfoByAuthorization(@RequestHeader("Authorization") String authorization) {
        String memberId = jwtUtils.parseAndReturnId(authorization);
        MemberResponseDto memberResponseDto = memberAdaptor.getMemberById(memberId).getBody();
        MemberInfoResponse result = new MemberInfoResponse(
                memberResponseDto.name(),
                memberResponseDto.loginId(),
                memberResponseDto.role());
        return ResponseEntity.ok().body(result);
    }

}
