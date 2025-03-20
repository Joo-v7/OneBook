package com.onebook.frontapi.dto.member;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record MemberFeignResponse(
//        Integer gradeId, // 1: REGULAR, 2: ROYAL, 3: GOLD, 4: PLATINUM
//        Integer roleId, // 1: MEMBER, 2: ADMIN
        String grade,
        String role,
        String name,
        String loginId,
        LocalDate dateOfBirth,
        String gender,
        String email,
        String phoneNumber,
        String status,
        LocalDateTime createdAt,
        LocalDateTime lastLoginAt
) {

}
