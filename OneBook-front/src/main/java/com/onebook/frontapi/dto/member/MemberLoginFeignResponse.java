package com.onebook.frontapi.dto.member;

public record MemberLoginFeignResponse(
        String loginId,
        String password,
        String role,
        String status
) {

}
