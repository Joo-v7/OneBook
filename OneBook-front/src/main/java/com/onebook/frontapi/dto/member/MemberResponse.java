package com.onebook.frontapi.dto.member;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record MemberResponse(
        String grade,
        String name,
        String loginId,
        LocalDate dateOfBirth,
        String gender,
        String email,
        String phoneNumber,
        String status,
        LocalDateTime createdAt,
        LocalDateTime lastLoginAt
)
{
    public static MemberResponse from(MemberFeignResponse memberFeignResponse) {
        return new MemberResponse(
//                convertGradeToString(memberFeignResponse.gradeId()),
                memberFeignResponse.grade(),
                maskName(memberFeignResponse.name()),
                memberFeignResponse.loginId(),
                memberFeignResponse.dateOfBirth(),
                memberFeignResponse.gender(),
                maskEmail(memberFeignResponse.email()),
                maskPhoneNumber(memberFeignResponse.phoneNumber()),
                memberFeignResponse.status(),
                memberFeignResponse.createdAt(),
                memberFeignResponse.lastLoginAt()
        );
    }

    // 등급 String 으로 변환.
//    private static String convertGradeToString(Integer gradeId) {
//        if(Objects.isNull(gradeId)) {
//            throw new IllegalArgumentException("GradeId is null");
//        }
//
//        switch (gradeId) {
//            case 1:
//                return "일반";
//            case 2:
//                return "로얄";
//            case 3:
//                return "골드";
//            case 4:
//                return "플래티넘";
//        }
//
//        throw new IllegalArgumentException("GradeId is Wrong");
//    }

    private static String makeMask(int number) {
        StringBuilder mask = new StringBuilder();

        for(int i = 0; i<number; i++) {
            mask.append("*");
        }

        return mask.toString();
    }

    // 이름 마스킹 처리 ex) 김주혁 -> 김*혁, 신사임당 -> 신*임당
    private static String maskName(String name) {
        String first = name.substring(0, 1);
        String last = name.substring(2);
        return first+"*"+last;
    }

    // 이메일 마스킹 처리 (예: helloworld@gmail.com -> hel******@gmail.com)
    private static String maskEmail(String email) {
        String[] split = email.split("@");

        String localPart = split[0];
        localPart = localPart.substring(0, 3) + makeMask(split[0].length() - 3);

        String domainPart = split[1];

        return localPart + "@" + domainPart;
    }

    // 전화번호 마스킹 처리 (예: 01012345678 -> 010-12**-5678)
    private static String maskPhoneNumber(String phoneNumber) {
        // 전화번호를 3부분으로 나누기
        String firstPart = phoneNumber.substring(0, 3); // 010
        String middlePart = phoneNumber.substring(3, 7); // 중간 4자리
        String endPart = phoneNumber.substring(7); // 마지막 4자리

        // 중간 부분 뒤 2자리 마스킹
        middlePart = middlePart.substring(0, 2) + makeMask(middlePart.length() - 2); // 첫 문자 유지, 나머지는 마스크

        // 마스킹 처리된 전화번호 반환 (하이픈 추가)
        return firstPart + "-" + middlePart + "-" + endPart;
    }


}
