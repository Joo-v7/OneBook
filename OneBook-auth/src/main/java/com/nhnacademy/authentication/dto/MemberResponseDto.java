package com.nhnacademy.authentication.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record MemberResponseDto(

        /**
         * Grade ID -> 1: REGULAR, 2: ROYAL, 3: GOLD, 4: PLATINUM
         * Role ID -> 1: MEMBER, 2: ADMIN
         */

//        Integer gradeId,
//        Integer roleId,
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
