package com.nhnacademy.taskapi.member.dto;

import com.nhnacademy.taskapi.member.domain.Member;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record MemberRegisterRequestDto(

        @NotBlank(message="회원 이름은 필수 입력 항목입니다.")
        @Size(max=10, message = "회원 이름은 10자 이하로 입력 가능합니다.")
        @Pattern(regexp = "^[가-힣]+$", message = "회원 이름은 한글만 입력 가능합니다.")
        String name,

        @NotBlank(message="로그인 아이디는 필수 입력 항목입니다.")
        @Size(max=20, message = "로그인 ID는 최대 20자까지 입력 가능합니다.")
        @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "영어 알파벳과 숫자만 입력 가능합니다.")
        String loginId,

        @NotBlank(message="로그인 비밀번호는 필수 입력 항목입니다.")
        @Size(min=10, max=20, message = "비밀 번호는 10 ~ 20자 이내로 입력 가능합니다.")
        @Pattern(regexp = "^[a-zA-Z0-9!@#$%^&*(),.?\":{}|<>\\-_+=~`'\\\\]+$", message = "영어 알파벳, 숫자, 특수 문자만 입력 가능합니다.")
        String password,

        @NotNull(message="생년월일은 필수 입력 항목입니다.")
        LocalDate dateOfBirth,

        @NotNull(message="성별은 필수 입력 항목입니다.")
        Member.Gender gender,

        @Email(message = "이메일 형식으로 작성해야 합니다.")
        String email,

        @NotBlank(message = "전화번호는 필수 입력 항목입니다.")
        @Pattern(regexp = "^01[0-9]\\d{7,8}$", message = "핸드폰 번호 형식이 올바르지 않습니다. (예: 01012345678)")
        String phoneNumber

) {}



