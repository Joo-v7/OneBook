package com.onebook.frontapi.dto.member;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record MemberResponseForAdmin(
        Long id,
        String grade,
        String role,
        String name,
        String loginId,
        String password,
        LocalDate dateOfBirth,
        String gender,
        String email,
        String phoneNumber,
        String status,
        LocalDateTime createdAt,
        LocalDateTime lastLoginAt
) {
}
