package com.onebook.frontapi.auth.userdetails;

import com.onebook.frontapi.auth.handler.MemberStatusValidator;
import com.onebook.frontapi.dto.member.MemberLoginFeignResponse;
import com.onebook.frontapi.feign.member.MemberFeignClient;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class FeignUserDetailsService implements UserDetailsService {

    private final MemberFeignClient memberFeignClient;
    private final MemberStatusValidator memberStatusValidator;

    // 로그인 때 동작.
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            ResponseEntity<MemberLoginFeignResponse> memberLoginResponseDtoResponseEntity = memberFeignClient.loadByMemberId(username);

            MemberLoginFeignResponse memberLoginFeignResponse = memberLoginResponseDtoResponseEntity.getBody();

            log.info("loginId: {}, password: {}, role: {}, status: {}", memberLoginFeignResponse.loginId(), memberLoginFeignResponse.password(), memberLoginFeignResponse.role(), memberLoginFeignResponse.status());

            // 멤버 상태 검증 후 그에 따른 적절한 예외처리.
            memberStatusValidator.validateMemberStatus(username, memberLoginFeignResponse.status());

            return new User(memberLoginFeignResponse.loginId(), memberLoginFeignResponse.password(), List.of(new SimpleGrantedAuthority("ROLE_" + memberLoginFeignResponse.role())));

        }catch(FeignException e) {
            throw new IllegalArgumentException("로그인에 실패하였습니다.");
        }
    }
}
