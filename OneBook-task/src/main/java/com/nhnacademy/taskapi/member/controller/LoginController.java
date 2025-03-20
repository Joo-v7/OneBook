package com.nhnacademy.taskapi.member.controller;

import com.nhnacademy.taskapi.member.domain.Member;
import com.nhnacademy.taskapi.member.dto.MemberLoginDto;
import com.nhnacademy.taskapi.member.dto.MemberLoginResponseDto;
import com.nhnacademy.taskapi.member.service.MemberService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RequiredArgsConstructor
@RestController
@Slf4j
@Tag(name = "Login", description = "로그인 검증, 로그인 회원 정보 조회, 로그인 기록 업데이트")  // API 그룹 설명 추가
public class LoginController {

    private final MemberService memberService;

    /**
     *
     * @return 로그인 시도하려는 아이디랑 패스워드랑 같으면 true, 아니면 false
     *
     * @author 수정자 문영호
     */
    @PostMapping("/task/auth/login")
    public ResponseEntity<Boolean> login(@RequestBody MemberLoginDto memberLoginDto) {

        MemberLoginDto result =  memberService.validateLogin(memberLoginDto);
        return ResponseEntity.ok().body( result != null );
    }

    // 로그인 시(UserDetailsService) 호출 - Authentication에 들어갈 내용
    @GetMapping("/task/auth/members/{loginId}")
    public ResponseEntity<MemberLoginResponseDto> loadByMemberId(@PathVariable(name = "loginId") String loginId){
        Member member = memberService.getMemberByLoginId(loginId);

        if(member != null){
            MemberLoginResponseDto result = new MemberLoginResponseDto(member.getLoginId(), member.getPassword(), member.getRole().getName(), String.valueOf(member.getStatus()));
            return ResponseEntity.of(Optional.of(result));
        }

        throw new RuntimeException();
    }

    // 멤버 로그인 기록 업데이트 by loginId -> 로그인 성공 후 SuccessHandler에서 호출.
    @PutMapping("/task/auth/{loginId}/login-history")
    public ResponseEntity<String> updateMemberLoginHistory(@PathVariable("loginId") String loginId) {
        memberService.updateMemberLoginId(loginId);
        return ResponseEntity.noContent().build();
    }

}
