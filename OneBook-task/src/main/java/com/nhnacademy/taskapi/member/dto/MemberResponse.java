package com.nhnacademy.taskapi.member.dto;

import com.nhnacademy.taskapi.member.domain.Member;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record MemberResponse(
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
    public static MemberResponse from(Member member) {
        return new MemberResponse(
                member.getId(),
                member.getGrade().getName(),
                member.getRole().getName(),
                member.getName(),
                member.getLoginId(),
                member.getPassword(),
                member.getDateOfBirth(),
                String.valueOf(member.getGender()),
                member.getEmail(),
                member.getPhoneNumber(),
                String.valueOf(member.getStatus()),
                member.getCreatedAt(),
                member.getLastLoginAt()
        );
    }
}
