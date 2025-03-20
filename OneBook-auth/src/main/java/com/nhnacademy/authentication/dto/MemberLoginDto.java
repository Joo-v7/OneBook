package com.nhnacademy.authentication.dto;

import jakarta.validation.constraints.NotBlank;

public record MemberLoginDto(
        @NotBlank(message="로그인 ID는 필수입니다.") String loginId,
        @NotBlank(message="로그인 PW는 필수입니다.") String password
) {}
