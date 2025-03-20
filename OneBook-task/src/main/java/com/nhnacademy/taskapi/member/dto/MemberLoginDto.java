package com.nhnacademy.taskapi.member.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record MemberLoginDto(

        @NotBlank(message="로그인 아이디는 필수 입력 항목입니다.")
        @Size(max=10, message = "로그인 ID는 최대 10자까지 입력 가능합니다.")
        @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "영어 알파벳과 숫자만 입력 가능합니다.")
        String loginId,

        @NotBlank(message="로그인 비밀번호는 필수 입력 항목입니다.")
        @Size(min=10, max=20, message = "비밀 번호는 10 ~ 20자 이내로 입력 가능합니다.")
        @Pattern(regexp = "^[a-zA-Z0-9!@#$%^&*(),.?\":{}|<>\\-_+=~`'\\\\]+$", message = "영어 알파벳, 숫자, 특수 문자만 입력 가능합니다.")
        String password

) {}
