package com.onebook.frontapi.auth.dto;

public record MemberInfoResponse(
        String name,
        String loginId,
        String role
) {
}
