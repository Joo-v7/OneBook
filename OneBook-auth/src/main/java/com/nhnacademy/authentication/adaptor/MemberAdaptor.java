package com.nhnacademy.authentication.adaptor;


import com.nhnacademy.authentication.dev_Member.JwtMemberDto;
import com.nhnacademy.authentication.dto.MemberLoginDto;
import com.nhnacademy.authentication.dto.MemberResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


// TODO task-service-dev -> task-service-prod로 바꿔주기도 해야함
@FeignClient(name = "task-service")
public interface MemberAdaptor {

    //    @PostMapping("/login")
//    public ResponseEntity<MemberLoginDto> postLogin(@RequestBody MemberLoginDto memberLoginDto);
    @PostMapping("/task/auth/login")
    public ResponseEntity<Boolean> login(@RequestBody MemberLoginDto memberLoginDto);

    @GetMapping("/task/auth/members")
    public ResponseEntity<MemberLoginDto> loadByMemberId(String loginId);

    @GetMapping("/task/members/jwt/{loginId}")
    public ResponseEntity<JwtMemberDto> getMemberForJWT(@PathVariable("loginId") String loginId);

    @GetMapping("/task/members/{memberId}")
    ResponseEntity<MemberResponseDto> getMemberById(@PathVariable("memberId") String memberId);


}


