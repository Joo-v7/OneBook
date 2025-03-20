package com.nhnacademy.taskapi.member.dto;

import com.nhnacademy.taskapi.member.domain.Member;
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
    public static MemberResponseDto from(Member member) {
        return new MemberResponseDto(
                member.getGrade().getName(),
                member.getRole().getName(),
                member.getName(),
                member.getLoginId(),
                member.getDateOfBirth(),
                String.valueOf(member.getGender()),
//                maskEmail(member.getEmail()),
//                maskPhoneNumber(member.getPhoneNumber()),
                member.getEmail(),
                member.getPhoneNumber(),
                String.valueOf(member.getStatus()),
                member.getCreatedAt(),
                member.getLastLoginAt()
        );
    }

//    private static String makeMask(int number) {
//        StringBuilder mask = new StringBuilder();
//
//        for(int i = 0; i<number; i++) {
//            mask.append("*");
//        }
//
//        return mask.toString();
//    }

//    // 이메일 마스킹 처리 (예: helloworld@gmail.com -> hel******@gmail.com)
//    private static String maskEmail(String email) {
//        String[] split = email.split("@");
//
//        String localPart = split[0];
//        localPart = localPart.substring(0, 3) + makeMask(split[0].length() - 3);
//
//        String domainPart = split[1];
//
//        return localPart + "@" + domainPart;
//    }
//
//    private static String maskPhoneNumber(String phoneNumber) {
//        // 전화번호를 3부분으로 나누기
//        String firstPart = phoneNumber.substring(0, 3); // 첫 3자리
//        String middlePart = phoneNumber.substring(3, 7); // 중간 4자리
//        String endPart = phoneNumber.substring(7); // 마지막 4자리
//
//        // 중간 부분과 마지막 부분을 마스킹
//        middlePart = middlePart.charAt(0) + makeMask(middlePart.length() - 1); // 첫 문자 유지, 나머지는 마스크
//        endPart = endPart.charAt(0) + makeMask(endPart.length() - 1); // 첫 문자 유지, 나머지는 마스크
//
//        // 마스킹 처리된 전화번호 반환 (하이픈 없이)
//        return firstPart + middlePart + endPart;
//    }

}
