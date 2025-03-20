package com.onebook.frontapi.feign.member;

import com.onebook.frontapi.dto.member.MemberLoginFeignResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@FeignClient(name = "TASK-SERVICE", url = "${onebook.gatewayUrl}")
@Component
public interface MemberFeignClient {
    @GetMapping("/task/auth/members/{loginId}")
    public ResponseEntity<MemberLoginFeignResponse> loadByMemberId(@PathVariable(name="loginId")String loginId);
}
