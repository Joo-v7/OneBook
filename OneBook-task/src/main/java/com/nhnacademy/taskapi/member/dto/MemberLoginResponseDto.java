package com.nhnacademy.taskapi.member.dto;

public record MemberLoginResponseDto(
        String loginId,
        String password,
        String role,
        String status // ('ACTIVE', 'INACTIVE', 'DELETED', 'SUSPENDED')
) {
}
