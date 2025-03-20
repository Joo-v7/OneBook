package com.nhnacademy.taskapi.member.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class JwtMemberDto {

    Long id;
    String loginId;
    String role;
}
