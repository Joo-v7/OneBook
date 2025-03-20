package com.nhnacademy.authentication.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@ToString
@NoArgsConstructor
@Setter
@Getter
public class LoginRequest {
    private String id;
    private String password;
}
