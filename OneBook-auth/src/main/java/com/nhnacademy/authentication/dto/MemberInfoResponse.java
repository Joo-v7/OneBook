package com.nhnacademy.authentication.dto;

public record MemberInfoResponse(
        String name,
        String loginId,
        String role
) {
}

